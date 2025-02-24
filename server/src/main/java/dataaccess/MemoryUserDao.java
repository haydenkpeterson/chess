package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDao implements UserDAO{
    final private ArrayList<UserData> userDataList = new ArrayList<>();

    @Override
    public void CreateUser(UserData userData) {
        userDataList.add(userData);
    }

    @Override
    public UserData ReadUser(UserData userData) throws DataAccessException {
        if (userDataList.contains(userData)){
            return userData;
        }
        else{
            return null;
        }
    }

    @Override
    public void DeleteUser(UserData userData) throws DataAccessException {
        userDataList.remove(userData);
    }
}
