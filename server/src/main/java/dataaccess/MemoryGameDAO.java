package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO{
    final private ArrayList<GameData> gameDataList = new ArrayList<>();

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        gameDataList.add(gameData);
    }

    @Override
    public void updateGame(AuthData authData, JoinData joinData) throws DataAccessException {
        GameData game = findGameFromId(joinData.gameID());
        if(game == null){
            throw new DataAccessException("Error: bad request");
        }
        String user = authData.username();
        gameDataList.remove(game);
        if(Objects.equals(joinData.playerColor(), "WHITE") && game.whiteUsername() == null) {
            gameDataList.add(new GameData(game.gameID(), user, game.blackUsername(), game.gameName(), game.game()));
        }
        else if(Objects.equals(joinData.playerColor(), "BLACK") && game.blackUsername() == null) {
            gameDataList.add(new GameData(game.gameID(), game.whiteUsername(), user, game.gameName(), game.game()));
        }
        else{
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public void makeMove(ChessGame game, int gameID) throws DataAccessException {
    }

    @Override
    public GameData findGameFromId(int gameID) throws DataAccessException {
        for(GameData game : gameDataList){
            if(game.gameID() == gameID){
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
