package websocket.messages;


import model.GameData;

public class LoadGameMessage extends ServerMessage{
    public GameData game;
    public LoadGameMessage(ServerMessageType type, GameData game) {
        super(type);
        this.game = game;
    }

    public GameData getGame() {
        return game;
    }
}
