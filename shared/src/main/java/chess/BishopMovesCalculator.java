package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends QueenShared {
    private Collection<ChessMove> validMoves = new ArrayList<>();

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece bishopPiece = board.getPiece(myPosition);
        diagonalMoves(board, myPosition, validMoves, bishopPiece);
        return validMoves;
    }
}
