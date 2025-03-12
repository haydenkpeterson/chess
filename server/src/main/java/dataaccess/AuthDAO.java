package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AuthDAO {
    void createAuth(AuthData auth) throws DataAccessException, SQLException;
    void deleteAuth(String authToken) throws DataAccessException, SQLException;
    AuthData findAuth(String authToken) throws DataAccessException, SQLException;
    void clearData();
    ArrayList<AuthData> listAuths();
}
