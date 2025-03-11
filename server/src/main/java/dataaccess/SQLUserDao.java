package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLUserDao implements UserDAO{
    private DatabaseManager manager;

    public SQLUserDao() {
        try {
            this.manager.configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?)")) {
            preparedStatement.setString(1, userData.username());
            preparedStatement.setString(2, hashPassword(userData.password()));
            preparedStatement.setString(3, userData.email());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData findUser(String username, String password) throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement(
                "SELECT username, password, email FROM users WHERE username = ?")) {
            try (var result = preparedStatement.executeQuery()) {
                return getUserData(result, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserData getUserData(ResultSet result, String username, String password) throws SQLException, DataAccessException {
        if (result.next()) {
            if(verifyUser(username, password)) {
                return new UserData(
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("email")
                );
            }
            else {
                return null;
            }
        }
        else {
            throw new DataAccessException("User not found.");
        }
    }

        @Override
    public void clearData() {

    }

    @Override
    public ArrayList<UserData> listUsers() {
        return null;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean verifyUser(String username, String password) throws SQLException, DataAccessException {
        var hashedPassword = readHashedPasswordFromDatabase(username);
        return BCrypt.checkpw(password, hashedPassword);
    }

    private String readHashedPasswordFromDatabase(String username) throws DataAccessException, SQLException {
        var conn = DatabaseManager.getConnection();
        var password = "";
        try (var preparedStatement = conn.prepareStatement("SELECT hashed_password FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            try (var result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    password = result.getString("hashed_password");
                }
            }
        }
        return password;
    }

}
