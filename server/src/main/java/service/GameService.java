package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.HashSet;
import java.util.Random;

import dataaccess.AuthDAO;
import record.JoinData;

import java.util.ArrayList;
import java.util.Set;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDao;
    private static final Random random = new Random();
    private static final Set<Integer> setID = new HashSet<>();


    public GameService(AuthDAO authDao, GameDAO gameDAO) {
        this.gameDAO = gameDAO;
        this.authDao = authDao;
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        if(getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }

    public AuthData getAuth(String authToken) {
        return authDao.findAuth(authToken);
    }

    public AuthData createAuth(AuthData authData) {
        authDao.createAuth(authData);
        return authData;
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        if(getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if(gameName == null) {
            throw new DataAccessException("Error: bad request");
        }
        if(getGame(gameName) != null) {
            throw new DataAccessException("Error: bad request");
        }
        else {
            int gameID = generateID();
            GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
            gameDAO.createGame(gameData);
            return gameData;
        }
    }

    public GameData getGame(String gameName){
        return gameDAO.getGame(gameName);
    }

    public void joinGame(String authToken, JoinData joinData) throws DataAccessException {
        AuthData authData = getAuth(authToken);
        if(authData == null)
        {
            throw new DataAccessException("Error: bad request");
        }
        gameDAO.updateGame(authData, joinData);
    }

    private int generateID() {
        int id;
        do{
            id = random.nextInt(10000);
        } while(setID.contains(id));
        setID.add(id);
        return id;
    }
}
