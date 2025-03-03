package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    private final ClearService clearService;


    public ClearHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.clearService = new ClearService(userDAO, authDAO, gameDAO);
    }

    public String clear(Request request, Response response) {
        try{
            clearService.clear();
            response.status(200);
            return "{}";
        } catch (Exception e) {
            response.status(500);
            response.body("{\"message\": \"Error: " + e.getMessage() + "\"}");
            return response.body();
        }
    }
}
