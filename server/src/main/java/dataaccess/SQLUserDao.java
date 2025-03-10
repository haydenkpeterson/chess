package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLUserDao implements UserDAO{

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?)")) {
            preparedStatement.setString(1, userData.username());
            preparedStatement.setString(2, userData.password());
            preparedStatement.setString(3, userData.email());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData findUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearData() {

    }

    @Override
    public ArrayList<UserData> listUsers() {
        return null;
    }
}
