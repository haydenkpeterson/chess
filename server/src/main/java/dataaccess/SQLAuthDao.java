package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class SQLAuthDao implements AuthDAO{
    @Override
    public void createAuth(AuthData auth) {

    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public AuthData findAuth(String authToken) {
        return null;
    }

    @Override
    public void clearData() {

    }

    @Override
    public ArrayList<AuthData> listAuths() {
        return null;
    }
}
