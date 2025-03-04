package chess;

import java.util.ArrayList;
import java.util.Collection;


public class PawnMovesCalculator {
    private Collection<ChessMove> validMoves = new ArrayList<>();

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece pawnPiece = board.getPiece(myPosition);

        /*WHITE MOVES*/
        if(pawnPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            /*CHECK AHEAD*/
            checkAhead(myPosition, board);

            /*CHECK TWO*/
            checkTwo(myPosition, board);

            /*CHECK TAKE*/
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            checkTake(myPosition, newPosition, board, pawnPiece);

            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            checkTake(myPosition, newPosition, board, pawnPiece);

            /*CHECK PROMOTE*/
            checkPromote(myPosition, board, pawnPiece);
        }

        /*BLACK MOVES*/
        else if(pawnPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {

            /*CHECK AHEAD*/
            checkAheadBlack(myPosition, board);

            /*CHECK TWO*/
            checkTwoBlack(myPosition, board);

            /*CHECK TAKE*/
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            checkTakeBlack(myPosition, newPosition, board, pawnPiece);

            newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            checkTakeBlack(myPosition, newPosition, board, pawnPiece);

            /*CHECK PROMOTE*/
            checkPromoteBlack(myPosition, board, pawnPiece);
        }
        return validMoves;
    }

    public void checkAhead(ChessPosition myPosition , ChessBoard board) {
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9)
                && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (myPosition.getRow() != 7) {
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                }
            }
        }
    }

    public void checkTwo(ChessPosition myPosition , ChessBoard board) {
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9)
                && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (myPosition.getRow() == 2) {
                if ((board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null)) {
                    if (board.getPiece(newPosition) == null) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        validMoves.add(move);
                    }
                }
            }
        }
    }

    public void checkTake(ChessPosition myPosition, ChessPosition newPosition, ChessBoard board, ChessPiece pawnPiece) {
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9)
                && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (myPosition.getRow() != 7) {
                takeAdd(myPosition, newPosition, board, pawnPiece);
            }
        }
    }

    public void checkPromote(ChessPosition myPosition, ChessBoard board, ChessPiece pawnPiece) {
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        if (newPosition.getRow() == 8) {
            if (board.getPiece(newPosition) == null) {
                validMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
            }
            if ((board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)) != null)
                    && board.getPiece(new ChessPosition(myPosition.getRow() + 1,
                    myPosition.getColumn() - 1)).getTeamColor() != pawnPiece.getTeamColor()) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1,
                        myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1,
                        myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1,
                        myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1,
                        myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
            }
            if ((board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)) != null)
                    && (board.getPiece(new ChessPosition(myPosition.getRow() + 1,
                    myPosition.getColumn() + 1)).getTeamColor() != pawnPiece.getTeamColor())) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1,
                        myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1,
                        myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1,
                        myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1,
                        myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
            }
        }
    }

    public void checkAheadBlack(ChessPosition myPosition, ChessBoard board) {
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9)
                && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (myPosition.getRow() != 2) {
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                }
            }
        }
    }

    public void checkTwoBlack(ChessPosition myPosition, ChessBoard board) {
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9)
                && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (myPosition.getRow() == 7) {
                if ((board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null)) {
                    if (board.getPiece(newPosition) == null) {
                        ChessMove move = new ChessMove(myPosition, newPosition, null);
                        validMoves.add(move);
                    }
                }
            }
        }
    }

    public void checkTakeBlack(ChessPosition myPosition, ChessPosition newPosition, ChessBoard board, ChessPiece pawnPiece) {
        if ((newPosition.getRow() >= 1 && newPosition.getRow() < 9)
                && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
            if (myPosition.getRow() != 2) {
                takeAdd(myPosition, newPosition, board, pawnPiece);
            }
        }
    }

    public void checkPromoteBlack(ChessPosition myPosition, ChessBoard board, ChessPiece pawnPiece) {
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
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
            if ((board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)) != null)
                    && (board.getPiece(new ChessPosition(myPosition.getRow() - 1,
                    myPosition.getColumn() - 1)).getTeamColor() != pawnPiece.getTeamColor())) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1,
                        myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1,
                        myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1,
                        myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1,
                        myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
            }
            if ((board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)) != null)
                    && (board.getPiece(new ChessPosition(myPosition.getRow() - 1,
                    myPosition.getColumn() + 1)).getTeamColor() != pawnPiece.getTeamColor())) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1,
                        myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1,
                        myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1,
                        myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1,
                        myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
            }
        }
    }

    public void takeAdd(ChessPosition myPosition, ChessPosition newPosition, ChessBoard board, ChessPiece pawnPiece) {
        if ((board.getPiece(newPosition) != null) && (board.getPiece(newPosition).getTeamColor() != pawnPiece.getTeamColor())) {
            ChessMove move = new ChessMove(myPosition, newPosition, null);
            validMoves.add(move);
        }
    }
}
