package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

public class RegisterService {
    private final UserDAO userDao;
    private final AuthDAO authDao;

    public RegisterService(UserDAO userDao, AuthDAO authDao) {
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
}

