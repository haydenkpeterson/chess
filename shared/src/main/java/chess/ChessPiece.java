package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor = null;
    private ChessPiece.PieceType type = null;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(board.getPiece(myPosition).getPieceType() == PieceType.KING) {
            KingMovesCalculator kingMoves = new KingMovesCalculator();
            return kingMoves.kingMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.ROOK) {
            RookMovesCalculator rookMoves = new RookMovesCalculator();
            return rookMoves.rookMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.BISHOP) {
            BishopMovesCalculator bishopMoves = new BishopMovesCalculator();
            return bishopMoves.bishopMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.QUEEN) {
            QueenMovesCalculator queenMoves = new QueenMovesCalculator();
            return queenMoves.queenMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT) {
            KnightMovesCalculator knightMoves = new KnightMovesCalculator();
            return knightMoves.knightMoves(board, myPosition);
        }
        if(board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            PawnMovesCalculator pawnMoves = new PawnMovesCalculator();
            return pawnMoves.pawnMoves(board, myPosition);
        }
        else {
            return null;
        }
    }
}
