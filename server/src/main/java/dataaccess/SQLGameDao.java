package dataaccess;

import model.AuthData;
import model.GameData;
import record.JoinData;

import java.util.ArrayList;

public class SQLGameDao implements GameDAO{
    @Override
    public void createGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public void updateGame(AuthData authData, JoinData joinData) throws DataAccessException {

    }

    @Override
    public GameData findGameFromId(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(String gameName) {
        return null;
    }

    @Override
    public void clearData() {

    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }
}
