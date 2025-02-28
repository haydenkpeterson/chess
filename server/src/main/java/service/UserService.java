package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final UserDAO userDao;
    private final AuthDAO authDao;

    public UserService(UserDAO userDao, AuthDAO authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public UserData getUser(String username) throws DataAccessException {
        return userDao.findUser(username);
    }

    public AuthData getAuth(String authToken) {
        return authDao.findAuth(authToken);
    }

    public AuthData createUser(UserData userData) throws DataAccessException {
        userDao.createUser(userData);
        String token = generateToken();
        return(createAuth(new AuthData(token, userData.username())));
    }

    public AuthData createAuth(AuthData authData) {
        authDao.createAuth(authData);
        return authData;
    }

    public AuthData loginUser(String username, String password) throws DataAccessException {
        UserData user = userDao.findUser(username);
        String token = generateToken();
        return(createAuth(new AuthData(token, user.username())));
    }

    public boolean logoutUser(String authToken) throws DataAccessException{
        authDao.deleteAuth(authToken);
        return true;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}

