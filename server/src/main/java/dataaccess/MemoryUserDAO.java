package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    final private ArrayList<UserData> userDataList = new ArrayList<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if(findUser(userData.username()) == null) {
            userDataList.add(userData);
        }
        else{
            throw new DataAccessException("Error: already taken");
        }
    }

    @Override
    public UserData findUser(String username) throws DataAccessException {
        for(UserData user: userDataList){
            if (Objects.equals(user.username(), username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(UserData userData) throws DataAccessException {
        userDataList.remove(userData);
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
