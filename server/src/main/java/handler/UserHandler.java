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
    private UserService userService;
    private final UserDAO userDao;
    private final AuthDAO authDao;

    public UserHandler(UserDAO userDao, AuthDAO authDao) {

        this.userDao = userDao;
        this.authDao = authDao;
    }

    public Response register(Request request, Response result) throws DataAccessException {
        String json = serializeRequest(request);
        UserData userData = deserializeToUser(json);
        AuthData authData = userService.createUser(userData);
        String authJson = serialize(authData);
        result = deserializeToResult(authJson);
        return result;
    }

    private UserData deserializeToUser(String json) {
        var serializer = new Gson();
        return serializer.fromJson(json, UserData.class);
    }

    private String serialize(AuthData authData) {
        var serializer = new Gson();
        return serializer.toJson(authData);
    }

    private String serializeRequest(Request request) {
        var serializer = new Gson();
        return serializer.toJson(request);
    }

    private Response deserializeToResult(String json) {
        var serializer = new Gson();
        return serializer.fromJson(json, Response.class);
    }
}
