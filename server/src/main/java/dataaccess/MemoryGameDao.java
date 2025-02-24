package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDao implements GameDAO{
    final private ArrayList<GameData> gameDataList = new ArrayList<>();

    @Override
    public void CreateGame(GameData gameData) throws DataAccessException {
        gameDataList.add(gameData);
    }

    @Override
    public void UpdateGame(GameData gameData) throws DataAccessException {
    }

    @Override
    public void DeleteGame(GameData gameData) throws DataAccessException {
        gameDataList.remove(gameData);
    }

    @Override
    public GameData ReadGame(GameData gameData) throws DataAccessException {
        if (gameDataList.contains(gameData)){
            return gameData;
        }
        else{
            return null;
        }
    }
}
