package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{
    final private ArrayList<GameData> gameDataList = new ArrayList<>();

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        gameDataList.add(gameData);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
    }

    @Override
    public void deleteGame(GameData gameData) throws DataAccessException {
        gameDataList.remove(gameData);
    }

    @Override
    public GameData readGame(GameData gameData) throws DataAccessException {
        if (gameDataList.contains(gameData)){
            return gameData;
        }
        else{
            return null;
        }
    }

    @Override
    public void clearData() {
        gameDataList.clear();
    }

}
