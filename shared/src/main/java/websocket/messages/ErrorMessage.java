package websocket.messages;

import chess.ChessGame;

public class ErrorMessage extends ServerMessage{
    public String errorMessage;
    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }
}
