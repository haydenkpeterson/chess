package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler {
    private final UserService userService;

    public UserHandler(UserDAO userDao, AuthDAO authDao) {
        this.userService = new UserService(userDao, authDao);
    }

    public String register(Request request, Response response) {
        try {
            UserData user = new Gson().fromJson(request.body(), UserData.class);
            if (user.username().isEmpty() || user.password().isEmpty() || user.email().isEmpty()) {
                response.status(400);
                response.body("{\"message\": \"Error: bad request\"}");
                return response.body();
            }
            AuthData authData = userService.createUser(user);
            response.status(200);
            var res = new Gson().toJson(authData);
            response.body(res);
            return response.body();
        } catch (DataAccessException e) {
            response.status(403);
            response.body("{\"message\": \"Error: already taken\"}");
            return response.body();
        } catch (Exception e) {
            response.status(500);
            response.body("{\"message\": \"Error: " + e.getMessage() + "\"}");
            return response.body();
        }
    }
}
