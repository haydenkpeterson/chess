package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{
    final private ArrayList<AuthData> authDataList = new ArrayList<>();

    @Override
    public void createAuth(AuthData authData) {
        authDataList.add(authData);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        boolean validToken = false;
        for(AuthData auth : authDataList) {
            if (Objects.equals(auth.authToken(), authToken)) {
                validToken = true;
                break;
            }
        }
        if(!validToken) {
            throw new DataAccessException("Error: unauthorized");
        }
        authDataList.removeIf(auth -> Objects.equals(auth.authToken(), authToken));
    }

    @Override
    public AuthData findAuth(String authToken) {
        for(AuthData auth : authDataList) {
            if(Objects.equals(auth.authToken(), authToken)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public void clearData() {
        authDataList.clear();
    }

    @Override
    public ArrayList<AuthData> listAuths() {
        return authDataList;
    }
}
