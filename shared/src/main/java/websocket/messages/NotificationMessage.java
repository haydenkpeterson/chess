package websocket.messages;

public class NotificationMessage extends ServerMessage{

    public NotificationMessage(ServerMessageType type, String message) {
        super(type);
    }

    public ServerMessageType type() {
        return getServerMessageType();
    }
}
