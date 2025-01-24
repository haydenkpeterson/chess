package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {
    private Collection<ChessMove> validMoves = new ArrayList<>();

    /**
     * Function for King moves
     */

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece king_piece = board.getPiece(myPosition);
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                if(!(i == 0 && j == 0)) {
                    ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j);
                    if((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)){
                        if (board.getPiece(newPosition) == null) {
                            ChessMove move = new ChessMove(myPosition, newPosition, null);
                            validMoves.add(move);
                        }
                        else {
                            if (board.getPiece(newPosition).getTeamColor() != king_piece.getTeamColor()) {
                                ChessMove move = new ChessMove(myPosition, newPosition, null);
                                validMoves.add(move);
                            }
                        }
                    }
                }
            }
        }
        return validMoves;
    }
}
