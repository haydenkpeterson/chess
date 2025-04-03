package websocket.commands;

import chess.ChessMove;
import chess.ChessPosition;

public class MakeMoveCommand extends UserGameCommand{
    public ChessMove move;

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        super(commandType, authToken, gameID);
        this.move = move;
    }
}
