package chess;

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
        if(piece != null) {
            return piece.pieceMoves(getBoard(), startPosition);
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
        ChessBoard board = getBoard();
        ChessPiece piece = board.getPiece(move.getStartPosition());
        setTeamTurn(piece.getTeamColor());
        try{
            if(move.getPromotionPiece() != null) {
                board.addPiece(move.getEndPosition(), piece);
                replaceNull(board, move);
            }
            else{
                ChessPiece promotionPiece = new ChessPiece(team, move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), promotionPiece);
                replaceNull(board, move);
                switchTurn(piece);
            }
        } catch (InvalidMoveException e) {
            throw new InvalidMoveException("Invalid Move" + e);
        }
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
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ChessPosition position = new ChessPosition(i, j);
                if(board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(position).getTeamColor() == teamColor){
                    kingPosition = position;
                }
            }
        }
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece enemyPiece = board.getPiece(position);
                if (enemyPiece.getTeamColor() == teamColor.opposite()) {
                    for(ChessMove move : enemyPiece.pieceMoves(board, position)){
                        if(move.getEndPosition() == kingPosition){
                            return true;
                        }
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
