package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;

import dataaccess.AuthDAO;
import model.JoinData;

import java.util.ArrayList;
import java.util.Set;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDao;
    private static final Random RANDOM = new Random();
    private static Set<Integer> setID = new HashSet<>();


    public GameService(AuthDAO authDao, GameDAO gameDAO) {
        this.gameDAO = gameDAO;
        this.authDao = authDao;
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException, SQLException {
        if(getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }

    protected AuthData getAuth(String authToken) throws SQLException, DataAccessException {
        return authDao.findAuth(authToken);
    }

    protected void createAuth(AuthData authData) throws SQLException, DataAccessException {
        authDao.createAuth(authData);
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException, SQLException {
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

    protected GameData getGame(String gameName) throws SQLException, DataAccessException {
        return gameDAO.getGame(gameName);
    }

    public String getUser(String authToken) throws SQLException, DataAccessException {
        AuthData auth = authDao.findAuth(authToken);
        return auth.username();
    }

    public void joinGame(String authToken, JoinData joinData) throws DataAccessException, SQLException {
        AuthData authData = getAuth(authToken);
        if(authData == null)
        {
            throw new DataAccessException("Error: unauthorized");
        }
        gameDAO.updateGame(authData, joinData);
    }

    private int generateID() {
        int id;
        do{
            id = RANDOM.nextInt(10000);
        } while(setID.contains(id));
        setID.add(id);
        return id;
    }
}
