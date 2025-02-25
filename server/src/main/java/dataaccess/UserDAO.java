package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData userData) throws DataAccessException;
    UserData readUser(UserData userData) throws DataAccessException;
    void deleteUser(UserData userData) throws DataAccessException;
    void clearData();
}
