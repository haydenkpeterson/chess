package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDAO {
    void createAuth(AuthData Auth);
    void deleteAuth(String authToken);
    AuthData readAuth(AuthData Auth);
    void clearData();
    ArrayList<AuthData> listAuths();
}
