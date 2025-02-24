package dataaccess;

import model.UserData;

public interface UserDAO {
    void CreateUser(UserData userData) throws DataAccessException;
    UserData ReadUser(UserData userData) throws DataAccessException;
    void DeleteUser(UserData userData) throws DataAccessException;
}
