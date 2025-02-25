package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO{
    final private ArrayList<UserData> userDataList = new ArrayList<>();

    @Override
    public void createUser(UserData userData) {
        userDataList.add(userData);
    }

    @Override
    public UserData readUser(UserData userData) throws DataAccessException {
        if (userDataList.contains(userData)){
            return userData;
        }
        else{
            return null;
        }
    }

    @Override
    public void deleteUser(UserData userData) throws DataAccessException {
        userDataList.remove(userData);
    }

    @Override
    public void clearData() {
        userDataList.clear();
    }
}
