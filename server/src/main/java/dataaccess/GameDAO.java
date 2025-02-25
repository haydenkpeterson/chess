package dataaccess;

import model.GameData;

public interface GameDAO {
    void createGame(GameData gameData) throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException;
    void deleteGame(GameData gameData) throws DataAccessException;
    GameData readGame(GameData gameData) throws DataAccessException;
    void clearData();
}
