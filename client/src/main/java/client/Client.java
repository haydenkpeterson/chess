package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.JoinData;
import serverfacade.ServerFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class Client {

    public enum State {
        SIGNEDOUT,
        SIGNEDIN,
        GAMEPLAY
    }

    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private AuthData auth;
    private Map<Integer, GameData> gameMap = new HashMap<>();
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private GameData storedGame;
    private String storedColor;

    public Client(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                case "list" -> listGames();
                case "logout" -> logout();
                case "join" -> joinGame(params);
                case "create" -> create(params);
                case "observe" -> observe(params);
                case "redraw" -> redraw();
                case "leave" -> leave();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if(state == State.SIGNEDOUT) {
            try {
                if (params.length == 3) {
                    state = State.SIGNEDIN;
                    String username = params[0];
                    visitorName = username;
                    String password = params[1];
                    String email = params[2];
                    UserData user = new UserData(username, password, email);
                    auth = server.register(user);
                    return String.format("Registered as %s.", user.username()) + "\n" + help();
                } else {
                    return "Expected <USERNAME> <PASSWORD> <EMAIL>";
                }
            } catch (ResponseException e) {
                state = State.SIGNEDOUT;
                return "Error registering";
            }
        }
        else{
            return "\n" + help();
        }
    }

    public String login(String... params) throws ResponseException {
        if(state == State.SIGNEDOUT) {
            try {
                if (params.length == 2) {
                    state = State.SIGNEDIN;
                    String username = params[0];
                    visitorName = username;
                    String password = params[1];
                    auth = server.login(new UserData(username, password, ""));
                    return String.format("Logged in as %s.", visitorName) + "\n" + help();
                } else {
                    return "Expected <USERNAME> <PASSWORD>";
                }
            } catch (ResponseException e) {
                state = State.SIGNEDOUT;
                return "Error logging in.";
            }
        }
        else{
            return "\n" + help();
        }
    }

    public String logout() throws ResponseException {
        if(state == State.SIGNEDIN) {
            try {
                assertSignedIn();
                if (auth != null) {
                    server.logout(auth);
                    state = State.SIGNEDOUT;
                    auth = null;
                    return String.format("%s logged out.", visitorName) + "\n" + help();
                } else {
                    return "Unauthorized.";
                }
            } catch (ResponseException e) {
                return "Error.";
            }
        }
        else {
            return "\n" + help();
        }
    }

    public String create(String... params) {
        if(state == State.SIGNEDIN) {
            try {
                if (auth != null) {
                    String name = params[0];
                    server.createGame(auth, new ServerFacade.Game(name));
                    return String.format("Created game: %s.", name);
                } else {
                    return "Unauthorized.";
                }
            } catch (ResponseException e) {
                return "Error.";
            }
        }
        else{
            return "\n" + help();
        }
    }

    public String listGames() throws ResponseException {
        if(state == State.SIGNEDIN) {
            try {
                if (auth != null) {
                    GameData[] games = server.listGames(auth);
                    int num = 1;
                    StringBuilder result = new StringBuilder();
                    for (GameData game : games) {
                        gameMap.put(num, game);
                        result.append(String.format("%d: white: %s, black: %s, game: %s\n",
                                num, game.whiteUsername(), game.blackUsername(), game.gameName()));
                        num++;
                    }
                    return result.toString();
                } else {
                    return "Unauthorized.";
                }
            } catch (ResponseException e) {
                return "Error.";
            }
        }
        else {
            return "\n" + help();
        }
    }

    public String makeJoin(String... params) throws ResponseException {
        int num = Integer.parseInt(params[0]);
        String color = params[1];
        for (Map.Entry<Integer, GameData> entry : gameMap.entrySet()) {
            if (entry.getKey() == num) {
                GameData game = entry.getValue();
                int gameID = game.gameID();
                server.updateGame(auth, new JoinData(color.toUpperCase(), gameID));
                storedColor = color;
                storedGame = game;

                ws = new WebSocketFacade(serverUrl, notificationHandler);
                ws.connect(auth.authToken(), gameID);

                state = State.GAMEPLAY;
                String board = createBoard(color.toUpperCase(), game.game());
                return board + "\n" + String.format("%s joined game %d as %s.", visitorName, num, color) + "\n" + help();
            }
        }
        return "Game does not exist.";
    }

    public String joinGame(String... params) {
        if(state == State.SIGNEDIN) {
            try {
                if (auth != null) {
                    try {
                        return makeJoin(params);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        return "Invalid game.";
                    }
                } else {
                    return "Unauthorized.";
                }
            } catch (ResponseException e) {
                return "Error.";
            }
        }
        else {
            return "\n" + help();
        }
    }

    public String observe(String... params) {
        if(state == State.SIGNEDIN) {
            try {
                int num = Integer.parseInt(params[0]);
                for (Map.Entry<Integer, GameData> entry : gameMap.entrySet()) {
                    if (entry.getKey() == num) {

                        GameData game = entry.getValue();
                        int gameID = game.gameID();
                        ws = new WebSocketFacade(serverUrl, notificationHandler);
                        ws.connect(auth.authToken(), gameID);

                        return createBoard("WHITE", game.game()) + "\n" + help();
                    }
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                return "Invalid game.";
            } catch (ResponseException e) {
                throw new RuntimeException(e);
            }
            return "Game does not exist.";
        }
        else {
            return "\n" + help();
        }
    }

    public String redraw() {
        if(state == State.GAMEPLAY) {
            return createBoard(storedColor.toUpperCase(), storedGame.game());
        }
        else {
            return "\n" + help();
        }
    }

    public String leave() throws ResponseException {
        if(state == State.GAMEPLAY) {
            try {
                int gameID = storedGame.gameID();
                ws = new WebSocketFacade(serverUrl, notificationHandler);
                ws.leave(auth.authToken(), gameID);
                state = State.SIGNEDIN;
                return "\n" + help();
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        else {
                return "\n" + help();
            }
    }

    public String[][] boardArrayWhite() {
        return new String[][]{
                {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP,
                        BLACK_QUEEN, BLACK_KING, BLACK_BISHOP,
                        BLACK_KNIGHT, BLACK_ROOK},
                {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN,
                        BLACK_PAWN, BLACK_PAWN, BLACK_PAWN,
                        BLACK_PAWN, BLACK_PAWN},
                {EMPTY, EMPTY, EMPTY, EMPTY,
                        EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY,
                        EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY,
                        EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY,
                        EMPTY, EMPTY, EMPTY, EMPTY},
                {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
                        WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
                        WHITE_PAWN, WHITE_PAWN},
                {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP,
                        WHITE_QUEEN, WHITE_KING, WHITE_BISHOP,
                        WHITE_KNIGHT, WHITE_ROOK}

        };
    }

    public String[][] boardArrayBlack() {
        return new String[][]{
                {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP,
                        WHITE_KING, WHITE_QUEEN, WHITE_BISHOP,
                        WHITE_KNIGHT, WHITE_ROOK},
                {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
                        WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
                        WHITE_PAWN, WHITE_PAWN},
                {EMPTY, EMPTY, EMPTY, EMPTY,
                        EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY,
                        EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY,
                        EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY,
                        EMPTY, EMPTY, EMPTY, EMPTY},
                {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN,
                        BLACK_PAWN, BLACK_PAWN, BLACK_PAWN,
                        BLACK_PAWN, BLACK_PAWN},
                {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP,
                        BLACK_KING, BLACK_QUEEN, BLACK_BISHOP,
                        BLACK_KNIGHT, BLACK_ROOK}
        };
    }

    public void addPieces(String[][] board, int i, StringBuilder boardDisplay) {
        for (int j = 0; j < board[i].length; j++) {
            boolean isLightSquare = (i + j) % 2 == 0;
            String squareColor;
            if (isLightSquare) {
                squareColor = SET_BG_COLOR_LIGHT_GREY;
            } else {
                squareColor = SET_BG_COLOR_DARK_GREY;
            }

            String pieceColor = getPieceColor(board, i, j);

            boardDisplay.append(squareColor)
                    .append(pieceColor)
                    .append(board[i][j])
                    .append(RESET_BG_COLOR)
                    .append(RESET_TEXT_COLOR);
        }
    }

    public String displayBoardBlack(ChessBoard chessBoard) {
        StringBuilder boardDisplay = new StringBuilder();
        String[][] board = reverseBoard(transformBoard(chessBoard));

        boardDisplay.append(RESET_TEXT_COLOR);
        boardDisplay.append("   h   g   f  e   d   c  b   a\n");

        for (int i = 0; i < board.length; i++) {
            boardDisplay.append(1 + i).append(" ");
            addPieces(board, i, boardDisplay);
            boardDisplay.append(" ").append(1 + i).append("\n");
        }
        boardDisplay.append("   h   g   f  e   d   c  b   a\n");
        return boardDisplay.toString();
    }

    private String createBoard(String color, ChessGame game) {
        if (Objects.equals(color, "WHITE")) {
            return displayBoardWhite(game.getBoard());
        }
        if (Objects.equals(color, "BLACK")) {
            return displayBoardBlack(game.getBoard());
        } else {
            return "Invalid Color";
        }
    }

    public String displayBoardWhite(ChessBoard chessBoard) {
        StringBuilder boardDisplay = new StringBuilder();
        String[][] board = transformBoard(chessBoard);

        boardDisplay.append(RESET_TEXT_COLOR);
        boardDisplay.append("   a   b   c  d   e   f  g   h\n");

        for (int i = 0; i < board.length; i++) {
            boardDisplay.append(8 - i).append(" ");
            addPieces(board, i, boardDisplay);
            boardDisplay.append(" ").append(8 - i).append("\n");
        }
        boardDisplay.append("   a   b   c  d   e   f  g   h\n");
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

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    - login <USERNAME> <PASSWORD> - to play chess
                    - quit - playing chess
                    - help - with possible commands
                    """;
        }
        if (state == State.GAMEPLAY) {
            return """
                    - redraw - board
                    - leave
                    - make move
                    - resign
                    - highlight - legal moves
                    """;
        }
        return """
                - create <name> - a game
                - list - games
                - join <ID> [WHITE|BLACK] - a game
                - observe <ID> - a game
                - logout - when you are done
                - quit
                - help - with possible commands
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

    private String[][] transformBoard(ChessBoard board) {
        String[][] transformBoard = new String[8][8];
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
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
                    }
                    else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
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
            }
        }
        return transformBoard;
    }

    public String[][] reverseBoard(String[][] board) {
        String[][] reversedBoard = new String[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                reversedBoard[8 - 1 - i][8 - 1 - j] = board[i][j];
            }
        }

        return reversedBoard;
    }
}
