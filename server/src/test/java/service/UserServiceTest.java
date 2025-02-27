package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {
    private final MemoryUserDAO userDAO = new MemoryUserDAO();
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static final UserService service = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());

    @BeforeEach
    void clear() {
        userDAO.clearData();
        authDAO.clearData();
    }

    @Test
    void createUser() throws DataAccessException {
        UserData user = new UserData("hp", "deeznuts", "pp@gmail.com");
        service.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        assertEquals(user, service.getUser("hp"));
    }

    @Test
    void loginUser() throws DataAccessException {
        service.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        assertTrue(service.loginUser("hp", "deeznuts"));
    }
    @Test
    void createAuth() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        assertEquals(auth, service.getAuth("token"));
    }

    @Test
    void logoutUser() throws DataAccessException {
        service.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        service.createAuth(new AuthData("token", "hp"));
        assertTrue(service.logoutUser("token"));
    }
}
