package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor team;
    private ChessBoard board;

    public ChessGame() {
        setBoard(new ChessBoard());
        this.board.resetBoard();
        this.team = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK;

        public TeamColor opposite() {
            if(this == WHITE){
                return BLACK;
            }
            else{
                return WHITE;
            }
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves;
        Collection<ChessMove> movesToRemove = new ArrayList<>();
        if(piece != null) {
            moves = piece.pieceMoves(getBoard(), startPosition);
            for(ChessMove move: moves){
                ChessPiece takenPiece = board.getPiece(move.getEndPosition());
                testMove(board, move);
                if(isInCheck(piece.getTeamColor())){
                    movesToRemove.add(move);
                }
                undoMove(board, move, piece, takenPiece);
            }
            moves.removeAll(movesToRemove);
            return moves;
        }
        else{
            return null;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        try {
            ChessBoard board = getBoard();
            if(board.getPiece(move.getStartPosition()) == null){
                throw new InvalidMoveException();
            }
            ChessPiece piece = board.getPiece(move.getStartPosition());
            if(piece.getTeamColor() != team){
                throw new InvalidMoveException();
            }
            if (!validMoves(move.getStartPosition()).contains(move)) {
                throw new InvalidMoveException("Invalid move for " + piece.getPieceType());
            }
            if (move.getPromotionPiece() == null) {
                board.addPiece(move.getEndPosition(), piece);
                replaceNull(board, move);
                switchTurn(piece);
            } else {
                ChessPiece promotionPiece = new ChessPiece(team, move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), promotionPiece);
                replaceNull(board, move);
                switchTurn(piece);
            }
        } catch (InvalidMoveException e) {
            throw new InvalidMoveException();
        }
    }

    /**
     * tests move
     * @param testBoard
     * @param move
     */
    public void testMove(ChessBoard testBoard, ChessMove move) {
        ChessPiece piece = testBoard.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() == null) {
            testBoard.addPiece(move.getEndPosition(), piece);
            replaceNull(testBoard, move);
        }
        else {
            ChessPiece promotionPiece = new ChessPiece(team, move.getPromotionPiece());
            testBoard.addPiece(move.getEndPosition(), promotionPiece);
            replaceNull(testBoard, move);
        }
    }

    public void undoMove(ChessBoard testBoard, ChessMove move, ChessPiece piece, ChessPiece takenPiece){

        testBoard.addPiece(move.getStartPosition(), piece);
        testBoard.addPiece(move.getEndPosition(), takenPiece);
    }

    /**
     * switches team after move
     * @param piece
     */
    public void switchTurn(ChessPiece piece){
        setTeamTurn(piece.getTeamColor().opposite());
    }


    /**
     * replaces the previous spot to null after a move
     *
     * @param board
     * @param move
     */
    public void replaceNull(ChessBoard board, ChessMove move){
        board.addPiece(move.getStartPosition(), null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                if(board.getPiece(position) != null) {
                    ChessPiece piece = board.getPiece(position);
                    if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                        kingPosition = position;
                    }
                }
            }
        }
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece enemyPiece = board.getPiece(position);
                if(isInCheckNull(position, enemyPiece, teamColor, kingPosition)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheckNull(ChessPosition position, ChessPiece enemyPiece, TeamColor teamColor, ChessPosition kingPosition){
        if(board.getPiece(position) != null) {
            if (enemyPiece.getTeamColor() == teamColor.opposite()) {
                for (ChessMove move : enemyPiece.pieceMoves(board, position)) {
                    if (move.getEndPosition().getRow() == kingPosition.getRow()
                            && move.getEndPosition().getColumn() == kingPosition.getColumn()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            //simulate all team moves and check if king is still in check
            for(int i = 1; i < 9; i++){
                for(int j = 1; j < 9; j++){
                    ChessPosition position = new ChessPosition(i, j);
                    if(!isInCheckmateNull(position, teamColor)){
                        return false;
                    }
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isInCheckmateNull(ChessPosition position, TeamColor teamColor) {
        if(board.getPiece(position) != null) {
            ChessPiece piece = board.getPiece(position);
            if (piece.getTeamColor() == teamColor) {
                Collection<ChessMove> possibleMoves = piece.pieceMoves(board, position);
                for(ChessMove move : possibleMoves){
                    ChessPiece takenPiece = board.getPiece(move.getEndPosition());
                    testMove(board, move);
                    if(!isInCheck(teamColor)){
                        undoMove(board, move, piece, takenPiece);
                        return false;
                    }
                    else{
                        undoMove(board, move, piece, takenPiece);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition position = new ChessPosition(i, j);
                    if(!isInStalemateNull(position, teamColor)){
                        return false;
                    }
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isInStalemateNull(ChessPosition position, TeamColor teamColor) {
        if (board.getPiece(position) != null) {
            ChessPiece piece = board.getPiece(position);
            if (piece.getTeamColor() == teamColor) {
                return validMoves(position).isEmpty();
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
