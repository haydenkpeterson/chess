package serverfacade;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import model.AuthData;
import model.JoinData;

import java.io.*;
import java.net.*;

public class ServerFacade {
    public record Game(String gameName){ }
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, null, AuthData.class);
    }

    public AuthData login(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, null, AuthData.class);
    }

    public void logout(AuthData auth) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, auth.authToken(), null);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public GameData[] listGames(AuthData auth) throws ResponseException {
        var path = "/game";
        record ListGameResponse(GameData[] games) {}
        var response = this.makeRequest("GET", path, null, auth.authToken(), ListGameResponse.class);

        return response.games();
    }

    public int createGame(AuthData auth, Game game) throws ResponseException {
        var path = "/game";
        record CreateGameResponse(int gameID) {}
        var response = this.makeRequest("POST", path, game, auth.authToken(), CreateGameResponse.class);
        return response.gameID();
    }

    public void updateGame(AuthData auth, JoinData join) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, join, auth.authToken(), null);
    }

    private <T> T makeRequest(String method, String path, Object request, String header, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setRequestProperty("authorization", header);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}