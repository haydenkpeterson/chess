package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

public class GameService {
    private final UserDAO userDao;
    private final AuthDAO authDao;
    private final GameDAO gameDAO;


    public GameService(UserDAO userDao, AuthDAO authDao, GameDAO gameDAO) {
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDAO = gameDAO;
    }

}
