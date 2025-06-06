package client;

import chess.*;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.JoinData;
import serverfacade.ServerFacade;

import java.util.*;

import static client.DrawBoard.*;

public class Client {

    public boolean isResign() {
        return resign;
    }

    public void setResign(boolean resign) {
        this.resign = resign;
    }

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
    private DrawBoard drawBoard;
    private boolean resign;

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
                case "move" -> makeMove(params);
                case "resign" -> resignAsk();
                case "highlight" -> highlight(params);
                case "yes" -> resign();
                case "no" -> no();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String no() {
        setResign(false);
        return "\n" + help();
    }

    private String resignAsk() {
        setResign(true);
        return "Are you sure you want to resign from this game? (yes/no)";
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
                return "\n" + String.format("%s joined game %d as %s.", visitorName, num, color) + "\n" + help();
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
                storedColor = null;
                int num = Integer.parseInt(params[0]);
                for (Map.Entry<Integer, GameData> entry : gameMap.entrySet()) {
                    if (entry.getKey() == num) {

                        GameData game = entry.getValue();
                        int gameID = game.gameID();
                        ws = new WebSocketFacade(serverUrl, notificationHandler);
                        ws.connect(auth.authToken(), gameID);

                        state = State.GAMEPLAY;
                        return "\n" + help();
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
            if(storedColor == null) {
                drawBoard = new DrawBoard(storedGame, storedColor, false);
                return drawBoard.createBoard("WHITE", storedGame.game(), null, null);
            }
            else {
                drawBoard = new DrawBoard(storedGame, storedColor.toUpperCase(), false);
                return drawBoard.createBoard(storedColor.toUpperCase(), storedGame.game(), null, null);
            }
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

    public String makeMove(String... params) {
        if(state == State.GAMEPLAY && storedColor != null) {
            try {
                String startPosition = params[0];
                String endPosition = params[1];
                String promotionPiece = params[2];

                ChessMove move = new ChessMove(new ChessPosition(getRow(startPosition), getColumn(startPosition))
                        , new ChessPosition(getRow(endPosition), getColumn(endPosition)),
                        getPromotion(promotionPiece));

                ws = new WebSocketFacade(serverUrl, notificationHandler);
                ws.makeMove(auth.authToken(), storedGame.gameID(), move);

                return "\n" + help();
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                return "Invalid move.";
            } catch (ResponseException e) {
                return e.toString();
            }
        }
        else {
            return "\n" + help();
        }
    }

    public String resign() {
        if(state == State.GAMEPLAY && storedColor != null && isResign()) {
            try {

                ws = new WebSocketFacade(serverUrl, notificationHandler);
                ws.resign(auth.authToken(), storedGame.gameID());

                return "\n" + help();
            } catch (ResponseException e) {
                return "Error: unable to resign";
            }
        }
        else {
            return "\n" + help();
        }
    }

    public String highlight(String... params) {
        if(state == State.GAMEPLAY) {
            boolean highlight = true;
            Collection<ChessMove> highlightedMoves;
            String startPosition = params[0];
            ChessPosition position = new ChessPosition(getRow(startPosition), getColumn(startPosition));
            highlightedMoves = storedGame.game().validMoves(position);
            if(storedColor == null) {
                drawBoard = new DrawBoard(storedGame, "WHITE", highlight);
                return drawBoard.createBoard("WHITE", storedGame.game(), position, highlightedMoves);
            }
            else {
                drawBoard = new DrawBoard(storedGame, storedColor.toUpperCase(), highlight);
                return drawBoard.createBoard(storedColor.toUpperCase(), storedGame.game(), position, highlightedMoves);
            }
        }
        else {
            return "\n" + help();
        }
    }

    public void setStoredGame(GameData game) {
        storedGame = game;
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
                    - move <start square|end square> [promotion piece] - makes a move
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
}
