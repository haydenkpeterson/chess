package service;

import model.AuthData;
import model.GameData;
import model.UserData;
import dataaccess.UserDAO;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;

public class ClearService {
    private final UserDAO userDao;
    private final GameDAO gameDao;
    private final AuthDAO authDao;

    public ClearService(UserDAO userDao, GameDAO gameDao, AuthDAO authDao) {
        this.userDao = userDao;
        this.gameDao = gameDao;
        this.authDao = authDao;
    }

    public void clear() {
        userDao.clearData();
        gameDao.clearData();
        authDao.clearData();
    }
}

