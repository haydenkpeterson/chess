package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import model.JoinData;
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
        }
    }

    private void connect(String token, int id, Session session) throws IOException, SQLException, DataAccessException {
        String user = service.getUser(token);

        connections.add(token, session, id);
        try {
            GameData game = service.getGameFromID(id);
            if(game == null) {
                var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error");
            }
            else if (!game.game().isInStalemate(getTeamColor(user,game)) ||
                    game.game().isInCheckmate(getTeamColor(user,game))) {
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "message");
            }
            else {
                LoadGameMessage loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);

            }
        }
        var message = String.format("%s connected as %s", user, "WHITE");
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(user, notification);
    }

    private ChessGame.TeamColor getTeamColor(String user, GameData game) {
        if(Objects.equals(user, game.whiteUsername())){
            return ChessGame.TeamColor.WHITE;
        }
        if(Objects.equals(user, game.blackUsername())){
            return ChessGame.TeamColor.BLACK;
        }
    }

    private void makeMove(String token, int id, ChessMove move, Session session) throws SQLException, DataAccessException, InvalidMoveException, IOException {
        String user = service.getUser(token);
        connections.add(user, session);
        service.makeMove(token, id, move);
        var message = String.format("%s connected as %s", user, "WHITE");
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(user, notification);
    }
}