package dataaccess;

import model.AuthData;
import model.GameData;
import record.JoinData;

import java.util.ArrayList;

public interface GameDAO {
    void createGame(GameData gameData) throws DataAccessException;
    void updateGame(AuthData authData, JoinData joinData) throws DataAccessException;
    GameData findGameFromId(int gameID) throws DataAccessException;
    GameData getGame(String gameName);
    void clearData();
    ArrayList<GameData> listGames();
}
