package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData Auth);
    void deleteAuth(AuthData Auth);
    AuthData readAuth(AuthData Auth);
    void clearData();
}
