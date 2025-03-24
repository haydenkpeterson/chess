package client;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import record.JoinData;
import serverfacade.ServerFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Client {

    public enum State {
        SIGNEDOUT,
        SIGNEDIN
    }

    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private AuthData auth;
    private Map<Integer, GameData> gameMap = new HashMap<>();

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
                case "observe" -> observe();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        try {
            if (params.length == 3) {
                state = State.SIGNEDIN;
                String username = params[0];
                visitorName = username;
                String password = params[1];
                String email = params[2];
                UserData user = new UserData(username, password, email);
                auth = server.register(user);
                return String.format("Registered as %s.", user.username());
            } else {
                return "Expected <USERNAME> <PASSWORD> <EMAIL>";
            }
        } catch (ResponseException e) {
            state = State.SIGNEDOUT;
            return "Error registering";
        }
    }

    public String login(String... params) throws ResponseException {
        try {
            if (params.length == 2) {
                state = State.SIGNEDIN;
                String username = params[0];
                visitorName = username;
                String password = params[1];
                auth = server.login(new UserData(username, password, ""));
                return String.format("Logged in as %s.", visitorName);
            } else {
                return "Expected <USERNAME> <PASSWORD>";
            }
        } catch (ResponseException e) {
            state = State.SIGNEDOUT;
            return "Error logging in.";
        }
    }

    public String logout() throws ResponseException {
        try {
            assertSignedIn();
            if (auth != null) {
                server.logout(auth);
                state = State.SIGNEDOUT;
                auth = null;
                return String.format("%s logged out.", visitorName);
            } else {
                return "Unauthorized.";
            }
        } catch (ResponseException e) {
            return "Error.";
        }
    }

    public String create(String... params) {
        try {
            if(auth != null) {
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

    public String listGames() throws ResponseException {
        try {
            if(auth != null) {
                GameData[] games = server.listGames(auth);
                int num = 1;
                StringBuilder result = new StringBuilder();
                for(GameData game : games) {
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

    public String joinGame(String... params) {
        try {
            if(auth != null) {
                int num = Integer.parseInt(params[0]);
                String color = params[1];
                for (Map.Entry<Integer, GameData> entry : gameMap.entrySet()) {
                    if (entry.getKey() == num) {
                        GameData game = entry.getValue();
                        int gameID = game.gameID();
                        server.updateGame(auth, new JoinData(color.toUpperCase(), gameID));
                        return String.format("%s joined game %d as %s.", visitorName, num, color);
                    }
                }
                return "Game does not exist.";
            } else {
                return "Unauthorized.";
            }
        } catch (ResponseException e) {
            return "Error.";
        }
    }

    public String observe() {
        return "Not Implemented.";
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
