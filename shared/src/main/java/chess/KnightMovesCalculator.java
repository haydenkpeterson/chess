package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {
    private Collection<ChessMove> validMoves = new ArrayList<>();

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece knightPiece = board.getPiece(myPosition);

        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
        addPiece(myPosition, newPosition, board, knightPiece);

        newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
        addPiece(myPosition, newPosition, board, knightPiece);

        newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
        addPiece(myPosition, newPosition, board, knightPiece);

        newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
        addPiece(myPosition, newPosition, board, knightPiece);

        newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
        addPiece(myPosition, newPosition, board, knightPiece);

        newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
        addPiece(myPosition, newPosition, board, knightPiece);

        newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
        addPiece(myPosition, newPosition, board, knightPiece);

        newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
        addPiece(myPosition, newPosition, board, knightPiece);

        return validMoves;
    }

    public void addPiece(ChessPosition myPosition, ChessPosition newPosition, ChessBoard board, ChessPiece knightPiece) {
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            } else if (board.getPiece(newPosition).getTeamColor() != knightPiece.getTeamColor()) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            }
        }
    }
}
