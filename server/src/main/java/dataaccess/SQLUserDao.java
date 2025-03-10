package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class SQLUserDao implements UserDAO{

    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }

    @Override
    public UserData findUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearData() {

    }

    @Override
    public ArrayList<UserData> listUsers() {
        return null;
    }
}
