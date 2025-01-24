package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {
    private Collection<ChessMove> validMoves = new ArrayList<>();

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece knight_piece = board.getPiece(myPosition);

        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            } else if (board.getPiece(newPosition).getTeamColor() != knight_piece.getTeamColor()) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            }
        }

        newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            } else if (board.getPiece(newPosition).getTeamColor() != knight_piece.getTeamColor()) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            }
        }
        newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            } else if (board.getPiece(newPosition).getTeamColor() != knight_piece.getTeamColor()) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            }
        }
        newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            } else if (board.getPiece(newPosition).getTeamColor() != knight_piece.getTeamColor()) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            }
        }
        newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            } else if (board.getPiece(newPosition).getTeamColor() != knight_piece.getTeamColor()) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            }
        }
        newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            } else if (board.getPiece(newPosition).getTeamColor() != knight_piece.getTeamColor()) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            }
        }
        newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            } else if (board.getPiece(newPosition).getTeamColor() != knight_piece.getTeamColor()) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            }
        }
        newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            } else if (board.getPiece(newPosition).getTeamColor() != knight_piece.getTeamColor()) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                validMoves.add(move);
            }
        }
        return validMoves;
    }
}
