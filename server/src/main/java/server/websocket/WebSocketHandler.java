package server.websocket;

import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.JoinData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;


@WebSocket
public class WebSocketHandler {
    private final GameService service;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(GameService service) {
        this.service = service;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, SQLException, DataAccessException {
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
        connections.add(user, session);
        service.joinGame(token, new JoinData("WHITE", id));
        var message = String.format("%s connected as %s", user, "WHITE");
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(user, notification);
    }

    private void makeMove(String token, int id, ChessMove move, Session session) throws SQLException, DataAccessException, InvalidMoveException {
        service.makeMove(token, id, move);
        var message = String.format("%s connected as %s", user, "WHITE");
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(user, notification);
    }
}