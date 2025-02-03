package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPosition position;
    private ChessPiece chess;
    private ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {

    }

    public ChessBoard(ChessBoard other){
        this.position = other.position;
        this.chess = other.chess;
        this.board = new ChessPiece[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.board[i][j] = (other.board[i][j]);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(position, that.position) && Objects.equals(chess, that.chess) && Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, chess, Arrays.deepHashCode(board));
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if(board[position.getRow()-1][position.getColumn()-1] != null){
            return board[position.getRow()-1][position.getColumn()-1];
        }
        else{
            return null;
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for(int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                if(i == 1) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
                }
                if(i == 6) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
                }
                if(i == 0 && (j == 0 || j == 7)) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                }
                if(i == 7 && (j == 0 || j == 7)) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                }
                if(i == 0 && (j == 1 || j == 6)) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                }
                if(i == 7 && (j == 1 || j == 6)) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                }
                if(i == 0 && (j == 2 || j == 5)) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                }
                if(i == 7 && (j == 2 || j == 5)) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                }
                if(i == 0 && j == 3) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                }
                if(i == 0 && j == 4) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                }
                if(i == 7 && j == 3) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                }
                if(i == 7 && j == 4) {
                    board[i][j] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                }
            }
        }

    }
}
