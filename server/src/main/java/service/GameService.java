package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import java.util.ArrayList;

public class GameService {
    private final UserDAO userDao;
    private final AuthDAO authDao;
    private final GameDAO gameDAO;


    public GameService(UserDAO userDao, AuthDAO authDao, GameDAO gameDAO) {
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDAO = gameDAO;
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        AuthData authData = authDao.findAuth(authToken);
        if(authData != null) {
            ArrayList<GameData> listOfGames = gameDAO.listGames();
            return listOfGames;
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        AuthData authData = authDao.findAuth(authToken);
        int gameID = 1;
        GameData gameData = new GameData(1, authData.username(), null, gameName, new ChessGame());
        gameDAO.createGame(gameData);
        return gameID;
    }

}
