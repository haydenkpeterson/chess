package client;

import chess.*;
import model.GameData;

import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class DrawBoard {

    private String storedColor;
    private boolean highlight;
    private GameData storedGame;

    public DrawBoard(GameData game, String color, boolean highlight) {
        this.storedGame = game;
        this.storedColor = color;
        this.highlight = highlight;
    }

    public static int getRow(String chessPosition) {
        return Integer.parseInt(chessPosition.substring(1));
    }

    public static int getColumn(String chessPosition) {
        char col = chessPosition.charAt(0);
        return Character.toLowerCase(col) - 'a' + 1;
    }

    public static ChessPiece.PieceType getPromotion(String piece) {
        if(Objects.equals(piece, "knight")) {
            return ChessPiece.PieceType.KNIGHT;
        }
        if(Objects.equals(piece, "queen")) {
            return ChessPiece.PieceType.QUEEN;
        }
        if(Objects.equals(piece, "bishop")) {
            return ChessPiece.PieceType.BISHOP;
        }
        if(Objects.equals(piece, "rook")) {
            return ChessPiece.PieceType.ROOK;
        }
        else{
            return null;
        }
    }

    public void addPieces(String[][] board, int i, StringBuilder boardDisplay, ChessPosition startPosition, Collection<ChessMove> highlightedMoves) {
        for (int j = 0; j < board[i].length; j++) {
            boolean isLightSquare = (i + j) % 2 == 0;
            String squareColor;

            if(highlight) {
                ChessPosition position;
                if(storedColor == null) {
                    position = new ChessPosition(8 - i, j + 1);
                }
                else if (Objects.equals(storedColor.toUpperCase(), "WHITE")) {
                    position = new ChessPosition(8 - i, j + 1);
                } else {
                    position = new ChessPosition(i + 1, 8 - j);
                }

                boolean isStartPosition = position.equals(startPosition);

                if (isStartPosition) {
                    squareColor = SET_BG_COLOR_YELLOW;
                } else if (isLegalMove(position, highlightedMoves)) {
                    squareColor = SET_BG_COLOR_GREEN;
                } else if (isLightSquare) {
                    squareColor = SET_BG_COLOR_LIGHT_GREY;
                } else {
                    squareColor = SET_BG_COLOR_DARK_GREY;
                }
            } else {
                if (isLightSquare) {
                    squareColor = SET_BG_COLOR_LIGHT_GREY;
                } else {
                    squareColor = SET_BG_COLOR_DARK_GREY;
                }
            }

            String pieceColor = getPieceColor(board, i, j);

            boardDisplay.append(squareColor)
                    .append(pieceColor)
                    .append(board[i][j])
                    .append(RESET_BG_COLOR)
                    .append(RESET_TEXT_COLOR);
        }
    }

    public boolean isLegalMove(ChessPosition position, Collection<ChessMove> highlightedMoves) {
        if (highlightedMoves != null) {
            for (ChessMove move : highlightedMoves) {
                if (move.getEndPosition().equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String displayBoardBlack(ChessBoard chessBoard, ChessPosition position, Collection<ChessMove> highlightedMoves) {
        StringBuilder boardDisplay = new StringBuilder();
        String[][] transformedBoard = transformBoard(chessBoard);
        String[][] board = new String[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][7-j] = transformedBoard[i][j];
            }
        }

        boardDisplay.append(RESET_TEXT_COLOR);
        boardDisplay.append("   h   g   f   e   d   c   b   a\n");

        for (int i = 0; i < board.length; i++) {
            boardDisplay.append(1 + i).append(" ");
            addPieces(board, i, boardDisplay, position, highlightedMoves);
            boardDisplay.append(" ").append(1 + i).append("\n");
        }
        boardDisplay.append("   h   g   f   e   d   c   b   a\n");
        setHighlight(false);
        return boardDisplay.toString();
    }

    public String createBoard(String color, ChessGame game, ChessPosition position, Collection<ChessMove> highlightedMoves) {
        if(highlight) {
            if (Objects.equals(color, "WHITE")) {
                return displayBoardWhite(game.getBoard(), position, highlightedMoves);
            }
            if (Objects.equals(color, "BLACK")) {
                return displayBoardBlack(game.getBoard(), position, highlightedMoves);
            } else {
                return displayBoardWhite(game.getBoard(), position, highlightedMoves);
            }
        }
        else {
            if (Objects.equals(color, "WHITE")) {
                return displayBoardWhite(game.getBoard(), null, null);
            }
            if (Objects.equals(color, "BLACK")) {
                return displayBoardBlack(game.getBoard(), null, null);
            } else {
                return "Invalid Color";
            }
        }
    }

    public String displayBoardWhite(ChessBoard chessBoard, ChessPosition position, Collection<ChessMove> highlightedMoves) {
        StringBuilder boardDisplay = new StringBuilder();
        String[][] transformedBoard = transformBoard(chessBoard);
        String[][] board = new String[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[7-i][j] = transformedBoard[i][j];
            }
        }

        boardDisplay.append(RESET_TEXT_COLOR);
        boardDisplay.append("   a   b   c   d   e   f   g   h\n");

        for (int i = 0; i < board.length; i++) {
            boardDisplay.append(8 - i).append(" ");
            addPieces(board, i, boardDisplay, position, highlightedMoves);
            boardDisplay.append(" ").append(8 - i).append("\n");
        }
        boardDisplay.append("   a   b   c   d   e   f   g   h\n");
        setHighlight(false);
        return boardDisplay.toString();
    }

    private static String getPieceColor(String[][] board, int i, int j) {
        String pieceColor;
        if (Objects.equals(board[i][j], WHITE_PAWN) ||
                Objects.equals(board[i][j], WHITE_ROOK) ||
                Objects.equals(board[i][j], WHITE_KNIGHT) ||
                Objects.equals(board[i][j], WHITE_BISHOP) ||
                Objects.equals(board[i][j], WHITE_QUEEN) ||
                Objects.equals(board[i][j], WHITE_KING)) {
            pieceColor = SET_TEXT_COLOR_BLUE;
        } else if (Objects.equals(board[i][j], BLACK_PAWN) ||
                Objects.equals(board[i][j], BLACK_ROOK) ||
                Objects.equals(board[i][j], BLACK_KNIGHT) ||
                Objects.equals(board[i][j], BLACK_BISHOP) ||
                Objects.equals(board[i][j], BLACK_QUEEN) ||
                Objects.equals(board[i][j], BLACK_KING)) {
            pieceColor = SET_TEXT_COLOR_MAGENTA;
        } else {
            pieceColor = "";
        }
        return pieceColor;
    }

    private String[][] transformBoard(ChessBoard board) {
        String[][] transformBoard = new String[8][8];
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                transformBoard = transform(board, transformBoard, i, j);
            }
        }
        return transformBoard;
    }

    private String[][] transform(ChessBoard board, String[][] transformBoard, int i, int j) {
        if (board.getBoard()[i][j] == null) {
            transformBoard[i][j] = EMPTY;
        } else {
            ChessPiece piece = board.getBoard()[i][j];

            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                switch (piece.getPieceType()) {
                    case KING:
                        transformBoard[i][j] = WHITE_KING;
                        break;
                    case QUEEN:
                        transformBoard[i][j] = WHITE_QUEEN;
                        break;
                    case BISHOP:
                        transformBoard[i][j] = WHITE_BISHOP;
                        break;
                    case KNIGHT:
                        transformBoard[i][j] = WHITE_KNIGHT;
                        break;
                    case ROOK:
                        transformBoard[i][j] = WHITE_ROOK;
                        break;
                    case PAWN:
                        transformBoard[i][j] = WHITE_PAWN;
                        break;
                }
            } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                switch (piece.getPieceType()) {
                    case KING:
                        transformBoard[i][j] = BLACK_KING;
                        break;
                    case QUEEN:
                        transformBoard[i][j] = BLACK_QUEEN;
                        break;
                    case BISHOP:
                        transformBoard[i][j] = BLACK_BISHOP;
                        break;
                    case KNIGHT:
                        transformBoard[i][j] = BLACK_KNIGHT;
                        break;
                    case ROOK:
                        transformBoard[i][j] = BLACK_ROOK;
                        break;
                    case PAWN:
                        transformBoard[i][j] = BLACK_PAWN;
                        break;
                }
            }
        }
        return transformBoard;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
}
