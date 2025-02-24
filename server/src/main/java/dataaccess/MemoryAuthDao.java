package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class MemoryAuthDao implements AuthDAO{
    final private ArrayList<AuthData> authDataList = new ArrayList<>();

    @Override
    public void createAuth(AuthData authData) {
        authDataList.add(authData);
    }

    @Override
    public void deleteAuth(AuthData authData) {
        authDataList.remove(authData);
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
}
