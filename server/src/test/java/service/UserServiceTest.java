package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final MemoryUserDAO userDAO = new MemoryUserDAO();
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static final UserService SERVICE = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());

    @BeforeEach
    void clear() {
        userDAO.clearData();
        authDAO.clearData();
    }

    @Test
    void createUser() throws DataAccessException {
        UserData user = new UserData("hp", "deeznuts", "pp@gmail.com");
        SERVICE.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        assertEquals(user, SERVICE.getUser("hp"));
    }

    @Test
    void createUserFail() throws DataAccessException {
        SERVICE.createUser(new UserData("hpeterson", "deeznuts", "pp@gmail.com"));
        assertThrows(DataAccessException.class, () -> SERVICE.createUser(new UserData("hpeterson", "deeznuts", "pp@gmail.com")), "Error: already taken");
    }

    @Test
    void loginUser() throws DataAccessException {
        AuthData authData = SERVICE.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        assertNotNull(SERVICE.loginUser("hp", "deeznuts").authToken());
    }

    @Test
    void loginUserFail() throws DataAccessException {
        SERVICE.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        assertThrows(DataAccessException.class, () -> SERVICE.loginUser("hp", "wrong password"), "Error: unauthorized");
    }

    @Test
    void createAuth() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        assertEquals(auth, SERVICE.getAuth("token"));
    }

    @Test
    void logoutUser() throws DataAccessException {
        SERVICE.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        SERVICE.createAuth(new AuthData("token", "hp"));
        assertTrue(SERVICE.logoutUser("token"));
    }

    @Test
    void logoutUserFail() throws DataAccessException {
        SERVICE.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        SERVICE.createAuth(new AuthData("token", "hp"));
        assertThrows(DataAccessException.class, () -> SERVICE.logoutUser("42"), "Error: unauthorized");
    }
}
