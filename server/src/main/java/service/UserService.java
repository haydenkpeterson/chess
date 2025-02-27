package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import java.util.Objects;

public class UserService {
    private final UserDAO userDao;
    private final AuthDAO authDao;

    public UserService(UserDAO userDao, AuthDAO authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public UserData getData(String username) throws DataAccessException {
        return userDao.findUser(username);
    }

    public void createUser(UserData userData) throws DataAccessException {
        userDao.createUser(userData);
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        authDao.createAuth(authData);
    }

    public boolean loginUser(String username, String password) throws DataAccessException {
        UserData user = userDao.findUser(username);
        return Objects.equals(user.Password(), password);
    }

    public boolean logoutUser(String authToken){
        authDao.deleteAuth(authToken);
        return true;
    }
}

