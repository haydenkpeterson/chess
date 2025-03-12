package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import record.JoinData;

import java.sql.SQLException;
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
        var games = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM user")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var name = rs.getString("username");
                        var password = rs.getString("password");

                        // Read and deserialize the friend JSON.
                        var json = rs.getString("friends");
                        var friends = new Gson().fromJson(json, String[].class);

                        users.add(new UserData(name, type, friends));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return games;
    }
}
