package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    final private ArrayList<UserData> userDataList = new ArrayList<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if(findUser(userData.username(), userData.password()) == null) {
            userDataList.add(userData);
        }
        else{
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public UserData findUser(String username, String password) throws DataAccessException {
        for(UserData user: userDataList){
            if (Objects.equals(user.username(), username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void clearData() {
        userDataList.clear();
    }

    @Override
    public ArrayList<UserData> listUsers() {
        return userDataList;
    }
}
