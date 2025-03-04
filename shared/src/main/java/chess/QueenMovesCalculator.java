package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends QueenShared {
    private Collection<ChessMove> validMoves = new ArrayList<>();

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece queenPiece = board.getPiece(myPosition);
        boolean block = false;

        /*DIAGONAL MOVES */
        diagonalMoves(board, myPosition, validMoves, queenPiece);

        /*LATERAL MOVES */
        verticalMoves(board, myPosition, validMoves, queenPiece);
        lateralMoves(board, myPosition, validMoves, queenPiece);

        return validMoves;
    }

}
