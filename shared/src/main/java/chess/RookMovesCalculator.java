package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends QueenShared{
    private Collection<ChessMove> validMoves = new ArrayList<>();

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece rookPiece = board.getPiece(myPosition);

        /*VERTICAL MOVES*/
        verticalMoves(board, myPosition, validMoves, rookPiece);

        /*LATERAL MOVES*/
        lateralMoves(board, myPosition, validMoves, rookPiece);
        return validMoves;
    }
}
