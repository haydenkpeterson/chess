package dataaccess;

import model.GameData;

public interface GameDAO {
    void CreateGame(GameData gameData) throws DataAccessException;
    void UpdateGame(GameData gameData) throws DataAccessException;
    void DeleteGame(GameData gameData) throws DataAccessException;
    GameData ReadGame(GameData gameData) throws DataAccessException;
}
