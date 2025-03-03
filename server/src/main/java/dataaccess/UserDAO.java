package dataaccess;

import model.UserData;

import java.util.ArrayList;

public interface UserDAO {
    void createUser(UserData userData) throws DataAccessException;
    UserData findUser(String username) throws DataAccessException;
    void clearData();
    ArrayList<UserData> listUsers();
}
