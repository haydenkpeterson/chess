package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDAO {
    void createAuth(AuthData auth);
    void deleteAuth(String authToken) throws DataAccessException;
    AuthData findAuth(String authToken);
    void clearData();
    ArrayList<AuthData> listAuths();
}
