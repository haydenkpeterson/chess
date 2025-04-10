package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {
    private final GameService service;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(GameService service) {
        this.service = service;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, SQLException, DataAccessException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
            case MAKE_MOVE -> {
                MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
            makeMove(moveCommand.getAuthToken(), moveCommand.getGameID(), moveCommand.move, session);
            }
            case RESIGN -> resign(command.getAuthToken(), command.getGameID(), session);
            case LEAVE -> leave(command.getAuthToken(), command.getGameID(), session);
        }
    }

    private void leave(String token, Integer id, Session session) throws IOException, SQLException, DataAccessException {
        if(service.verifyAuth(token) == null) {
            var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid authToken");
            connections.sendMsg(session, token, error);
            return;
        }
        String user = service.getUser(token);
        GameData game = service.getGameFromID(id);
        if(Objects.equals(game.whiteUsername(), user) || Objects.equals(game.blackUsername(), user)) {
            service.leave(getTeamColor(user, game), id);
        }
        var notification = new NotificationMessage(
                ServerMessage.ServerMessageType.NOTIFICATION, user + " has left");
        connections.broadcast(id, token, notification);
        connections.remove(token);
    }

    private void resign(String token, Integer id, Session session) throws SQLException, DataAccessException, IOException {
        if(service.verifyAuth(token) == null) {
            var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid auth");
            connections.sendMsg(session, token, error);
            return;
        }
        String user = service.getUser(token);
        try {
            GameData game = service.getGameFromID(id);
            if(game == null) {
                var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid game");
                connections.sendMsg(session, token, error);
            }
            else {
                if(!game.game().getGameOver()) {
                    if (getTeamColor(user, game) == null) {
                        var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error");
                        connections.sendMsg(session, token, error);
                    } else {
                        game.game().setGameOver(true);
                        service.resign(game, id);
                        var notification = new NotificationMessage(
                                ServerMessage.ServerMessageType.NOTIFICATION, user + " has resigned");
                        connections.broadcast(id, token, notification);
                        connections.sendMsg(session, token, notification);
                    }
                }
                else {
                    var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game is over");
                    connections.sendMsg(session, token, error);
                }
            }
        } catch (SQLException | DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(String token, int id, Session session) throws IOException, SQLException, DataAccessException {
        if(service.verifyAuth(token) == null) {
            var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error");
            connections.sendMsg(session, token, error);
            return;
        }
        String user = service.getUser(token);
        connections.add(token, session, id);
        try {
            GameData game = service.getGameFromID(id);
            if(game == null) {
                var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error");
                connections.sendMsg(session, token, error);
            }
            else {
                LoadGameMessage loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
                connections.sendMsg(session, token, loadGame);
                if(getTeamColor(user, game) == null) {
                    var notification = new NotificationMessage(
                            ServerMessage.ServerMessageType.NOTIFICATION, user + " connected to game as observer");
                    connections.broadcast(id, token, notification);
                }
                else {
                    var notification = new NotificationMessage(
                            ServerMessage.ServerMessageType.NOTIFICATION,
                            user + " connected to game as " + Objects.requireNonNull(getTeamColor(user, game)));
                    connections.broadcast(id, token, notification);
                }
            }
        } catch (SQLException | DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ChessGame.TeamColor getTeamColor(String user, GameData game) {
        if(Objects.equals(user, game.whiteUsername())){
            return ChessGame.TeamColor.WHITE;
        }
        if(Objects.equals(user, game.blackUsername())){
            return ChessGame.TeamColor.BLACK;
        }
        else{
            return null;
        }
    }

    private void makeMove(String token, int id, ChessMove move, Session session)
            throws SQLException, DataAccessException, InvalidMoveException, IOException {
        if(service.verifyAuth(token) == null) {
            var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid authToken");
            connections.sendMsg(session, token, error);
            return;
        }
        String user = service.getUser(token);
        try {
            GameData game = service.getGameFromID(id);
            if(game == null) {
                var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid game");
                connections.sendMsg(session, token, error);
            }
            else {
                if(!game.game().getGameOver()) {
                    try {
                        service.makeMove(token, id, move, getTeamColor(user, game));
                    } catch (InvalidMoveException e) {
                        var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move");
                        connections.sendMsg(session, token, error);
                        return;
                    }
                    GameData changedGame = service.getGameFromID(id);
                    LoadGameMessage loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, changedGame);
                    connections.broadcast(id, token, loadGame);
                    var message = String.format("%s made move %s to %s", user, move.getStartPosition().toString(), move.getEndPosition().toString());
                    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                    connections.broadcast(id, token, notification);

                    String oppositeName = null;
                    if(getOppositeColor(game, user) == ChessGame.TeamColor.WHITE) {
                        oppositeName = game.whiteUsername();
                    }
                    if(getOppositeColor(game, user) == ChessGame.TeamColor.BLACK) {
                        oppositeName = game.blackUsername();
                    }
                    if(isInCheckmate(game, user)){
                        message = String.format("%s is in checkmate", oppositeName);
                        notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                        connections.broadcast(id, token, notification);
                        connections.sendMsg(session, token, notification);
                    } else if (isInCheck(game, user)) {
                        message = String.format("%s is now in check", oppositeName);
                        notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                        connections.broadcast(id, token, notification);
                        connections.sendMsg(session, token, notification);
                    }
                }
                else{
                    var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game over");
                    connections.sendMsg(session, token, error);
                }
            }
        } catch (SQLException | DataAccessException | IOException e) {
            var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: retry");
            connections.sendMsg(session, token, error);
        }
    }

    public Boolean isInCheck(GameData gameData, String user) {
        ChessGame.TeamColor otherColor = getOppositeColor(gameData, user);
        return gameData.game().isInCheck(otherColor);
    }

    public Boolean isInCheckmate(GameData gameData, String user) {
        ChessGame.TeamColor otherColor = getOppositeColor(gameData, user);
        return gameData.game().isInCheckmate(otherColor);
    }

    private ChessGame.TeamColor getOppositeColor(GameData gameData, String user) {
        ChessGame.TeamColor otherColor;
        if(getTeamColor(user, gameData) == ChessGame.TeamColor.WHITE) {
            otherColor = ChessGame.TeamColor.BLACK;
        }
        else {
            otherColor = ChessGame.TeamColor.WHITE;
        }
        return otherColor;
    }
}