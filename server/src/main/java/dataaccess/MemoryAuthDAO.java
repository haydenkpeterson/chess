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
    public void deleteAuth(String authToken) {
        authDataList.removeIf(auth -> Objects.equals(auth.authToken(), authToken));
    }

    @Override
    public AuthData readAuth(AuthData authData) {
        if (authDataList.contains(authData)){
            return authData;
        }
        else{
            return null;
        }
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
