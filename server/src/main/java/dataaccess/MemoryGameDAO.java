package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO{
    final private ArrayList<GameData> gameDataList = new ArrayList<>();

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        gameDataList.add(gameData);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        GameData game = findGameFromID(gameData);
        gameDataList.remove(game);
        if(gameData.whiteUsername() != null && game.whiteUsername() == null) {
            gameDataList.add(new GameData(game.gameID(), gameData.whiteUsername(), game.blackUsername(), game.gameName(), game.game()));
        }
        else if(gameData.blackUsername() != null && game.blackUsername() == null) {
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
    public GameData findGameFromID(GameData gameData) throws DataAccessException {
        for(GameData game : gameDataList){
            if(game.gameID() == gameData.gameID()){
                return game;
            }
        }
        return null;
    }

    @Override
    public GameData getGame(String gameName) {
        for(GameData game : gameDataList) {
            if(Objects.equals(game.gameName(), gameName)){
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
