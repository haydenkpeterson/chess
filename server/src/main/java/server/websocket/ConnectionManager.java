package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> gameConnections
            = new ConcurrentHashMap<>();

    public void add(String token, Session session, int gameID) {
        var connection = new Connection(token, session, gameID);
        connections.put(token, connection);
        gameConnections.computeIfAbsent(gameID, k -> new ConcurrentHashMap<>())
                .put(token, connection);
    }

    public void remove(String token) {
        for (var game : gameConnections.values()) {
            if (game.remove(token) != null) {
                break;
            }
        }
    }

    public void broadcast(int gameID, String excludeAuth, ServerMessage message) throws IOException {
        ConcurrentHashMap<String, Connection> gameMap = gameConnections.get(gameID);
        if (gameMap == null) {
            return;
        }
        var removeList = new ArrayList<Connection>();
        if(message instanceof LoadGameMessage loadGameMessage) {
            for (var c : gameMap.values()) {
                if (c.session.isOpen()) {
                    String msg = new Gson().toJson(loadGameMessage);
                    c.send(msg);
                } else {
                    removeList.add(c);
                }
            }
        }
        if(message instanceof NotificationMessage notificationMessage) {
            for (var c : gameMap.values()) {
                if (c.session.isOpen()) {
                    if (!c.token.equals(excludeAuth)) {
                        String msg = new Gson().toJson(notificationMessage);
                        c.send(msg);
                    }
                } else {
                    removeList.add(c);
                }
            }
        }

        // Clean up any connections that were left open
        for (var c : removeList) {
            gameMap.remove(c.token);
        }
    }

    public void sendMsg(Session session, String token, ServerMessage message) throws IOException {
        Connection connection = connections.get(token);
        if (connection == null) {
            if (session.isOpen()) {
                String msg = new Gson().toJson(message);
                session.getRemote().sendString(msg);
            }
            return;
        }
        if (connection.session.isOpen()) {
            if (message instanceof LoadGameMessage loadGameMessage) {
                String msg = new Gson().toJson(loadGameMessage);
                connection.send(msg);
            }
            if (message instanceof ErrorMessage errorMessage) {
                String msg = new Gson().toJson(errorMessage);
                connection.send(msg);
            }
            if (message instanceof NotificationMessage notificationMessage) {
                String msg = new Gson().toJson(notificationMessage);
                connection.send(msg);
            }
        }
    }
}