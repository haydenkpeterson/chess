package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator {
    private Collection<ChessMove> validMoves = new ArrayList<>();

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece queen_piece = board.getPiece(myPosition);
        boolean block = false;

        /*DIAGONAL MOVES */
        for(int i = 1; i < 9; i++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            if((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (board.getPiece(newPosition) == queen_piece){
                    continue;
                }
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                } else if (board.getPiece(newPosition).getTeamColor() != queen_piece.getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                    block = true;
                } else {
                    block = true;
                }
            }
        }
        for(int i = 1; i < 9; i++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
            if((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (board.getPiece(newPosition) == queen_piece){
                    continue;
                }
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                } else if (board.getPiece(newPosition).getTeamColor() != queen_piece.getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                    block = true;
                } else {
                    block = true;
                }
            }
        }
        for(int j = 1; j < 9; j++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - j, myPosition.getColumn() + j);
            if((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (board.getPiece(newPosition) == queen_piece){
                    continue;
                }
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                } else if (board.getPiece(newPosition).getTeamColor() != queen_piece.getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                    block = true;
                } else {
                    block = true;
                }
            }
        }
        for(int j = 1; j < 9; j++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + j, myPosition.getColumn() - j);
            if((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (board.getPiece(newPosition) == queen_piece){
                    continue;
                }
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                } else if (board.getPiece(newPosition).getTeamColor() != queen_piece.getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                    block = true;
                } else {
                    block = true;
                }
            }
        }

        /*LATERAL MOVES */
        for(int i = 1; i < 9; i++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
            if((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (board.getPiece(newPosition) == queen_piece){
                    continue;
                }
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                } else if (board.getPiece(newPosition).getTeamColor() != queen_piece.getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                    block = true;
                } else {
                    block = true;
                }
            }
        }
        for(int i = 1; i < 9; i++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
            if((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (board.getPiece(newPosition) == queen_piece){
                    continue;
                }
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                } else if (board.getPiece(newPosition).getTeamColor() != queen_piece.getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                    block = true;
                } else {
                    block = true;
                }
            }
        }
        for(int j = 1; j < 9; j++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j);
            if((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (board.getPiece(newPosition) == queen_piece){
                    continue;
                }
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                } else if (board.getPiece(newPosition).getTeamColor() != queen_piece.getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                    block = true;
                } else {
                    block = true;
                }
            }
        }
        for(int j = 1; j < 9; j++) {
            if(block){
                block = false;
                break;
            }
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - j);
            if((newPosition.getRow() >= 1 && newPosition.getRow() < 9) && (newPosition.getColumn() >= 1 && newPosition.getColumn() < 9)) {
                if (board.getPiece(newPosition) == queen_piece){
                    continue;
                }
                if (board.getPiece(newPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                } else if (board.getPiece(newPosition).getTeamColor() != queen_piece.getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(move);
                    block = true;
                } else {
                    block = true;
                }
            }
        }
        return validMoves;
    }

}
