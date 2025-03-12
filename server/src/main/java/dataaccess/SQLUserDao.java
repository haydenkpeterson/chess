package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLUserDao implements UserDAO{

    public SQLUserDao() {
        try {
            DatabaseManager manager = new DatabaseManager();
            manager.configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?, ?)")) {
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
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE tableNameHere")) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<UserData> listUsers() {
        var users = new ArrayList<UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM user")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var name = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");
                        users.add(new UserData(name, password, email));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return users;
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
