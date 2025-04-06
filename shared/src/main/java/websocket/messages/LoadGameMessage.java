package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    public LoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type);
    }
}
