package server;

import dataaccess.*;
import handler.UserHandler;
import handler.ClearHandler;
import handler.GameHandler;
import spark.*;

public class Server {
    private final UserDAO userDAO = new SQLUserDao();
    private final AuthDAO authDAO = new SQLAuthDAO();
    private final GameDAO gameDAO = new SQLGameDao();

    public Server() {

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", new UserHandler(userDAO, authDAO)::register);
        Spark.post("/session", new UserHandler(userDAO, authDAO)::login);
        Spark.delete("/session", new UserHandler(userDAO, authDAO)::logout);
        Spark.delete("/db", new ClearHandler(userDAO, authDAO, gameDAO)::clear);
        Spark.post("/game", new GameHandler(authDAO, gameDAO)::createGame);
        Spark.get("/game", new GameHandler(authDAO, gameDAO)::listGames);
        Spark.put("/game", new GameHandler(authDAO, gameDAO)::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
