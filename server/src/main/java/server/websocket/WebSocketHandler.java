package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
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
            case LEAVE -> leave();
            case MAKE_MOVE -> makeMove();
            case RESIGN -> resign();
            case CONNECT -> connect(command.getAuthToken(), session);
        }
    }

    private void connect(String token, Session session) throws IOException, SQLException, DataAccessException {
        String user = service.getUser(token);
        connections.add(user, session);
        var message = String.format("%s connected as %s", user, );
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(user, notification);
    }

    private void exit(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new NotificationMessage(NotificationMessage.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }

    public void makeNoise(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new NotificationMessage(NotificationMessage.Type.NOISE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}