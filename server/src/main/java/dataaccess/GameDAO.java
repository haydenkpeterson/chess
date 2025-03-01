package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void createGame(GameData gameData) throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException;
    void deleteGame(GameData gameData) throws DataAccessException;
    GameData findGame(GameData gameData) throws DataAccessException;
    void clearData();
    ArrayList<GameData> listGames();
}
