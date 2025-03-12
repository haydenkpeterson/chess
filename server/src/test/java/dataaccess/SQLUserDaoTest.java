package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDaoTest {
    private final SQLUserDao sqlUserDao = new SQLUserDao();

    @BeforeEach
    void clear() {
        sqlUserDao.clearData();
    }

    @Test
    void createUserPass() throws DataAccessException, SQLException {
        UserData user = new UserData("hp", "deeznuts", "pp@gmail.com");
        sqlUserDao.createUser(user);
        assertNotNull(sqlUserDao.findUser(user.username(), user.password()));
    }

    @Test
    void createUserFail() throws DataAccessException, SQLException {
        UserData user = new UserData("hp", "deeznuts", "pp@gmail.com");
        sqlUserDao.createUser(user);
        assertThrows(RuntimeException.class, () -> {
            sqlUserDao.createUser(user);
        });
    }

    @Test
    void findUserPass() throws DataAccessException, SQLException {
        UserData user = new UserData("hp", "deeznuts", "pp@gmail.com");
        sqlUserDao.createUser(user);
        assertEquals(user.username(), sqlUserDao.findUser(user.username(), user.password()).username());
    }

    @Test
    void findUserFail() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            sqlUserDao.findUser("fakeuser", "fakepassword");
        });
    }

    @Test
    void clearData() throws DataAccessException, SQLException {
        UserData user = new UserData("hp", "deeznuts", "pp@gmail.com");
        sqlUserDao.createUser(user);
        sqlUserDao.clearData();
        assertTrue(sqlUserDao.listUsers().isEmpty());
    }
}
