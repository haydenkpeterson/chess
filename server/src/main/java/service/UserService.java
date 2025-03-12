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

    protected UserData getUser(String username) throws DataAccessException {
        return userDao.findUser(username, null);
    }

    protected AuthData getAuth(String authToken) {
        return authDao.findAuth(authToken);
    }

    public AuthData createUser(UserData userData) throws DataAccessException {
        userDao.createUser(userData);
        String token = generateToken();
        return(createAuth(new AuthData(token, userData.username())));
    }

    protected AuthData createAuth(AuthData authData) {
        authDao.createAuth(authData);
        return authData;
    }

    public AuthData loginUser(String username, String password) throws DataAccessException {
        UserData user;
        if(userDao.findUser(username, password) != null) {
            user = userDao.findUser(username, password);
        }
        else{
            throw new DataAccessException("Error: unauthorized");
        }
        if(!Objects.equals(password, user.password())){
            throw new DataAccessException("Error: unauthorized");
        }
        String token = generateToken();
        return(createAuth(new AuthData(token, user.username())));
    }

    public boolean logoutUser(String authToken) throws DataAccessException{
        authDao.deleteAuth(authToken);
        return true;
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}

