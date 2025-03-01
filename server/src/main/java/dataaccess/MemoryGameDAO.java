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
        GameData game = findGame(gameData);
        gameDataList.remove(game);
        if(!gameData.whiteUsername().isEmpty() && game.whiteUsername().isEmpty()) {
            gameDataList.add(new GameData(game.gameID(), gameData.whiteUsername(), game.blackUsername(), game.gameName(), game.game()));
        }
        else if(!gameData.blackUsername().isEmpty() && game.blackUsername().isEmpty()) {
            gameDataList.add(new GameData(game.gameID(), game.whiteUsername(), gameData.blackUsername(), game.gameName(), game.game()));
        }
        else{
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public void deleteGame(GameData gameData) throws DataAccessException {
        gameDataList.remove(gameData);
    }

    @Override
    public GameData findGame(GameData gameData) throws DataAccessException {
        for(GameData game : gameDataList){
            if(game.gameID() == gameData.gameID()){
                return game;
            }
        }
        return null;
    }

    @Override
    public void clearData() {
        gameDataList.clear();
    }

    @Override
    public ArrayList<GameData> listGames() {
        return gameDataList;
    }
}
