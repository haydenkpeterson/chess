package chess;

import java.util.ArrayList;
import java.util.Collection;


public class PawnMovesCalculator {
    private Collection<ChessMove> validMoves = new ArrayList<>();

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece pawn_piece = board.getPiece(myPosition);

        /*WHITE MOVES*/
        if(pawn_piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            /*CHECK AHEAD*/
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (myPosition.getRow() != 7) {
                    if (board.getPiece(newPosition) == null) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        validMoves.add(move);
                    }
                }
            }

            /*CHECK TWO*/
            newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
            if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (myPosition.getRow() == 2) {
                    if ((board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null)) {
                        if (board.getPiece(newPosition) == null) {
                            ChessMove move = new ChessMove(myPosition, newPosition, null);
                            validMoves.add(move);
                        }
                    }
                }
            }

            /*CHECK TAKE*/
            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (myPosition.getRow() != 7) {
                    if ((board.getPiece(newPosition) != null) && (board.getPiece(newPosition).getTeamColor() != pawn_piece.getTeamColor())) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        validMoves.add(move);
                    }
                }
            }

            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (myPosition.getRow() != 7) {
                    if ((board.getPiece(newPosition) != null) && (board.getPiece(newPosition).getTeamColor() != pawn_piece.getTeamColor())) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        validMoves.add(move);
                    }
                }
            }

            /*CHECK PROMOTE*/
            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (newPosition.getRow() == 8) {
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN);
                    validMoves.add(move);
                    move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT);
                    validMoves.add(move);
                    move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK);
                    validMoves.add(move);
                    move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP);
                    validMoves.add(move);
                }
                if ((board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)) != null) && board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)).getTeamColor() != pawn_piece.getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
                }
                if ((board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor() != pawn_piece.getTeamColor())) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
                }
            }
        }

        /*BLACK MOVES*/
        else if(pawn_piece.getTeamColor() == ChessGame.TeamColor.BLACK) {

            /*CHECK AHEAD*/
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (myPosition.getRow() != 2) {
                    if (board.getPiece(newPosition) == null) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        validMoves.add(move);
                    }
                }
            }

            /*CHECK TWO*/
            newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
            if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (myPosition.getRow() == 7) {
                    if ((board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null)) {
                        if (board.getPiece(newPosition) == null) {
                            ChessMove move = new ChessMove(myPosition, newPosition, null);
                            validMoves.add(move);
                        }
                    }
                }
            }

            /*CHECK TAKE*/
            newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (myPosition.getRow() != 2) {
                    if ((board.getPiece(newPosition) != null) && (board.getPiece(newPosition).getTeamColor() != pawn_piece.getTeamColor())) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        validMoves.add(move);
                    }
                }
            }
            newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (myPosition.getRow() != 2) {
                    if ((board.getPiece(newPosition) != null) && (board.getPiece(newPosition).getTeamColor() != pawn_piece.getTeamColor())) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        validMoves.add(move);
                    }
                }
            }

            /*CHECK PROMOTE*/
            newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if (newPosition.getRow() == 1) {
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN);
                    validMoves.add(move);
                    move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT);
                    validMoves.add(move);
                    move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK);
                    validMoves.add(move);
                    move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP);
                    validMoves.add(move);
                }
                if ((board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)).getTeamColor() != pawn_piece.getTeamColor())) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
                }
                if ((board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)) != null) && (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)).getTeamColor() != pawn_piece.getTeamColor())) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
                }
            }
        }
        return validMoves;
    }
}
