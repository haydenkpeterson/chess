package chess;

import java.util.Collection;

public class QueenShared {
    boolean block = false;

    public void diagonalMoves(ChessBoard board, ChessPosition myPosition,
                              Collection<ChessMove> validMoves, ChessPiece piece) {
        boolean block = false;
        for(int i = 1; i < 9; i++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            if(checkBounds(newPosition)){
                if (board.getPiece(newPosition) == piece){
                    continue;
                }
                block = addPiece(myPosition, newPosition, validMoves, board, piece);
            }
        }
        for(int i = 1; i < 9; i++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
            if(checkBounds(newPosition)){
                if (board.getPiece(newPosition) == piece){
                    continue;
                }
                block = addPiece(myPosition, newPosition, validMoves, board, piece);
            }
        }
        for(int j = 1; j < 9; j++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - j, myPosition.getColumn() + j);
            if(checkBounds(newPosition)){
                if (board.getPiece(newPosition) == piece){
                    continue;
                }
                block = addPiece(myPosition, newPosition, validMoves, board, piece);
            }
        }
        for(int j = 1; j < 9; j++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + j, myPosition.getColumn() - j);
            if(checkBounds(newPosition)){
                if (board.getPiece(newPosition) == piece){
                    continue;
                }
                block = addPiece(myPosition, newPosition, validMoves, board, piece);
            }
        }
    }

    public void verticalMoves(ChessBoard board, ChessPosition myPosition,
                              Collection<ChessMove> validMoves, ChessPiece piece) {
        for (int i = 1; i < 9; i++) {
            if (block) {
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
            if(checkBounds(newPosition)){
                if (board.getPiece(newPosition) == piece){
                    continue;
                }
                block = addPiece(myPosition, newPosition, validMoves, board, piece);
            }
        }

        for (int i = 1; i < 9; i++) {
            if (block) {
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
            if(checkBounds(newPosition)){
                if (board.getPiece(newPosition) == piece){
                    continue;
                }
                block = addPiece(myPosition, newPosition, validMoves, board, piece);
            }
        }
    }

    public void lateralMoves(ChessBoard board, ChessPosition myPosition,
                             Collection<ChessMove> validMoves, ChessPiece piece) {
        for(int j = 1; j < 9; j++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j);
            if(checkBounds(newPosition)){
                if (board.getPiece(newPosition) == piece){
                    continue;
                }
                block = addPiece(myPosition, newPosition, validMoves, board, piece);
            }
        }
        for(int j = 1; j < 9; j++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - j);
            if(checkBounds(newPosition)){
                if (board.getPiece(newPosition) == piece){
                    continue;
                }
                block = addPiece(myPosition, newPosition, validMoves, board, piece);
            }
        }
    }

    public boolean addPiece(ChessPosition myPosition, ChessPosition newPosition,
                            Collection<ChessMove> validMoves, ChessBoard board, ChessPiece piece) {
        if (board.getPiece(newPosition) == null) {
            ChessMove move = new ChessMove(myPosition, newPosition, null);
            validMoves.add(move);
        } else if (board.getPiece(newPosition).getTeamColor() != piece.getTeamColor()) {
            ChessMove move = new ChessMove(myPosition, newPosition, null);
            validMoves.add(move);
            return true;
        } else {
            return true;
        }
        return false;
    }

    public boolean checkBounds(ChessPosition newPosition) {
        return (newPosition.getRow() >= 1 && newPosition.getRow() < 9)
                && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9);
    }
}
