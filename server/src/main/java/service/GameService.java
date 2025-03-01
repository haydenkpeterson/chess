package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.HashSet;
import java.util.Random;

import dataaccess.AuthDAO;

import java.util.ArrayList;
import java.util.Set;

public class GameService {
    private final GameDAO gameDAO;
    private static final Random random = new Random();
    private static final Set<Integer> setID = new HashSet<>();


    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public ArrayList<GameData> listGames() {
        return gameDAO.listGames();
    }

    public int createGame(String gameName) throws DataAccessException {
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
            return gameID;
        }
    }

    public GameData getGame(String gameName){
        return gameDAO.getGame(gameName);
    }

    public boolean joinGame(GameData gameData) throws DataAccessException {
        gameDAO.updateGame(gameData);
        return true;
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
