package client;

import exception.ResponseException;
import model.AuthData;
import model.UserData;
import serverfacade.ServerFacade;

import java.util.Arrays;

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
                case "observe" -> observe(params);
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
                String password = params[1];
                String email = params[2];
                auth = server.register(new UserData(username, password, email));
                return String.format("Registered as %s.", visitorName);
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
                String password = params[1];
                auth = server.login(new UserData(username, password, ""));
                return String.format("Logged in as %s.", visitorName);
            } else {
                return "Expected <USERNAME> <PASSWORD>";
            }
        } catch (ResponseException e) {
            state = State.SIGNEDOUT;
            return "Error logging in";
        }
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        if(auth != null) {
            server.logout(auth);
            state = State.SIGNEDOUT;
            auth = null;
            return String.format("%s logged out", visitorName);
        }
        else {
            return "unauthorized";
        }
    }

    public String listGames(String... params) throws ResponseException {
        try {
            if (params.length == 3) {
                state = State.SIGNEDIN;
                String username = params[0];
                String password = params[1];
                String email = params[2];
                auth = server.register(new UserData(username, password, email));
                return String.format("Registered as %s.", visitorName);
            } else {
                return "Expected <USERNAME> <PASSWORD> <EMAIL>";
            }
        } catch (ResponseException e) {
            state = State.SIGNEDOUT;
            return "Error registering";
        }
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
