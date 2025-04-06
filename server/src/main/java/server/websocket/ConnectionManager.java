package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String token, Session session, int gameID) {
        var connection = new Connection(token, session, gameID);
        connections.put(token, connection);
    }

    public void remove(String name) {
        connections.remove(name);
    }

    public void broadcast(String excludeVisitorName, NotificationMessage notificationMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.token.equals(excludeVisitorName)) {
                    c.send(notificationMessage.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.token);
        }
    }

    public void sendMsg(String token, ServerMessage message) throws IOException {
        Connection connection = connections.get(token);
        if (connection != null) {
            if (connection.session.isOpen()) {
                connection.send(message.toString());
            } else {
                connections.remove(token);
            }
        }
    }
}