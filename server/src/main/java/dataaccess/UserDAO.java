package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserDAO {
    void createUser(UserData userData) throws DataAccessException, SQLException;
    UserData findUser(String username, String password) throws DataAccessException;
    void clearData();
    ArrayList<UserData> listUsers();
}
