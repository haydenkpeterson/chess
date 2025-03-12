package dataaccess;

import model.AuthData;
import model.GameData;
import record.JoinData;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GameDAO {
    void createGame(GameData gameData) throws DataAccessException, SQLException;
    void updateGame(AuthData authData, JoinData joinData) throws DataAccessException;
    GameData findGameFromId(int gameID) throws DataAccessException;
    GameData getGame(String gameName) throws DataAccessException, SQLException;
    void clearData();
    ArrayList<GameData> listGames();
}
