package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashSet;
import java.util.Random;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import java.util.ArrayList;
import java.util.Set;

public class GameService {
    private final UserDAO userDao;
    private final AuthDAO authDao;
    private final GameDAO gameDAO;
    private static final Random random = new Random();
    private static final Set<Integer> setID = new HashSet<>();


    public GameService(UserDAO userDao, AuthDAO authDao, GameDAO gameDAO) {
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDAO = gameDAO;
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        AuthData authData = authDao.findAuth(authToken);
        if(authData != null) {
            return gameDAO.listGames();
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        AuthData authData = authDao.findAuth(authToken);
        if(authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        int gameID = generateID();
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDAO.createGame(gameData);
        return gameID;
    }

    public boolean joinGame(String authToken, GameData gameData) throws DataAccessException {
        AuthData authData = authDao.findAuth(authToken);
        if(authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        gameDAO.updateGame(gameData);
        return true;
    }

    public int generateID() {
        int id;
        do{
            id = random.nextInt(10000);
        } while(setID.contains(id));
        setID.add(id);
        return id;
    }
}
