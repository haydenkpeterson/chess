package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final MemoryUserDAO userDao = new MemoryUserDAO();
    private final MemoryAuthDAO authDao = new MemoryAuthDAO();
    private final UserService service = new UserService(userDao, authDao);

    @BeforeEach
    void clear() {
        userDao.clearData();
        authDao.clearData();
    }

    @Test
    void createUser() throws DataAccessException, SQLException {
        UserData user = new UserData("hp", "deeznuts", "pp@gmail.com");
        service.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        assertEquals(user, service.getUser("hp"));
    }

    @Test
    void createUserFail() throws DataAccessException, SQLException {
        service.createUser(new UserData("hpeterson", "deeznuts", "pp@gmail.com"));
        assertThrows(DataAccessException.class, () ->
                service.createUser(new UserData("hpeterson", "deeznuts", "pp@gmail.com")), "Error: already taken");
    }

    @Test
    void loginUser() throws DataAccessException, SQLException {
        service.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        assertNotNull(service.loginUser("hp", "deeznuts").authToken());
    }

    @Test
    void loginUserFail() throws DataAccessException, SQLException {
        service.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        assertThrows(DataAccessException.class, () -> service.loginUser("hp", "wrong password"), "Error: unauthorized");
    }

    @Test
    void createAuth() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        assertEquals(auth, service.getAuth("token"));
    }

    @Test
    void logoutUser() throws DataAccessException, SQLException {
        service.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        service.createAuth(new AuthData("token", "hp"));
        assertTrue(service.logoutUser("token"));
    }

    @Test
    void logoutUserFail() throws DataAccessException, SQLException {
        service.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        service.createAuth(new AuthData("token", "hp"));
        assertThrows(DataAccessException.class, () -> service.logoutUser("42"), "Error: unauthorized");
    }
}
