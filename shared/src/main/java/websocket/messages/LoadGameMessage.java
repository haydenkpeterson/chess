package websocket.messages;


import model.GameData;

public class LoadGameMessage extends ServerMessage{
    public LoadGameMessage(ServerMessageType type, GameData game) {
        super(type);
    }
}
