package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import record.ListData;
import record.CreateData;
import service.GameService;
import spark.Request;
import spark.Response;
import record.JoinData;

import java.util.Objects;

public class GameHandler {
    static class Game {
        String gameName;
    }

    private final GameService gameService;

    public GameHandler(AuthDAO authDao, GameDAO gameDao) {
        this.gameService = new GameService(authDao, gameDao);
    }

    public String createGame(Request request, Response response) {
        try {
            String authToken = request.headers("Authorization");
            Game game = new Gson().fromJson(request.body(), Game.class);
            GameData gameData = gameService.createGame(authToken, game.gameName);
            response.status(200);
            CreateData createData = new CreateData(gameData.gameID());
            return (new Gson().toJson(createData));
        } catch (DataAccessException e) {
            response.status(401);
            response.body("{\"message\": \"Error: unauthorized\"}");
            return response.body();
        }
    }

    public String listGames(Request request, Response response) {
        try {
            String authToken = request.headers("Authorization");
            ListData gameList = new ListData(gameService.listGames(authToken));
            response.status(200);
            return new Gson().toJson(gameList);
        } catch (DataAccessException e) {
            response.status(401);
            response.body("{\"message\": \"Error: unauthorized\"}");
            return response.body();
        }
    }

    public String joinGame(Request request, Response response) {
        try {
            String authToken = request.headers("Authorization");
            JoinData joinData = new Gson().fromJson(request.body(), JoinData.class);
            if (!Objects.equals(joinData.playerColor(), "WHITE") && !Objects.equals(joinData.playerColor(), "BLACK")) {
                response.status(400);
                response.body("{\"message\": \"Error: bad request\"}");
                return response.body();
            }
            gameService.joinGame(authToken, joinData);
            response.status(200);
            return "{}";
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: bad request")) {
                response.status(400);
                response.body("{\"message\": \"Error: bad request\"}");
                return response.body();
            }
            if (Objects.equals(e.getMessage(), "Error: already taken")) {
                response.status(403);
                response.body("{\"message\": \"Error: already taken\"}");
                return response.body();
            }
            else {
                response.status(401);
                response.body("{\"message\": \"Error: unauthorized\"}");
                return response.body();
            }
        }
    }
}
