package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDaoTest {
    private final SQLAuthDao sqlAuthDao = new SQLAuthDao();

    @BeforeEach
    void clear() {
        sqlAuthDao.clearData();
    }

    @Test
    void createAuthPass() throws SQLException, DataAccessException {
        sqlAuthDao.createAuth(new AuthData("token", "hp"));
        assertNotEquals("", sqlAuthDao.findAuth("token").authToken());
    }

    @Test
    void createAuthFail() throws SQLException, DataAccessException {
        sqlAuthDao.createAuth(new AuthData("token", "hp"));
        assertThrows(RuntimeException.class, () -> {
            sqlAuthDao.createAuth(new AuthData("token", "hp"));
        });
    }

    @Test
    void deleteAuthPass() throws SQLException, DataAccessException {
        sqlAuthDao.createAuth(new AuthData("token", "hp"));
        sqlAuthDao.deleteAuth("token");
        assertTrue(sqlAuthDao.listAuths().isEmpty());
    }

    @Test
    void deleteAuthFail() {
        assertThrows(DataAccessException.class, () -> {
            sqlAuthDao.deleteAuth("token");
        });
    }

    @Test
    void findAuthPass() throws SQLException, DataAccessException {
        sqlAuthDao.createAuth(new AuthData("token", "hp"));
        assertNotNull(sqlAuthDao.findAuth("token"));
    }

    @Test
    void findAuthFail() throws SQLException, DataAccessException {
        assertNull(sqlAuthDao.findAuth("token"));
    }

    @Test
    void clearData() throws SQLException, DataAccessException {
        sqlAuthDao.createAuth(new AuthData("token", "hp"));
        sqlAuthDao.clearData();
        assertTrue(sqlAuthDao.listAuths().isEmpty());
    }

    @Test
    void listAuthsPass() throws SQLException, DataAccessException {
        sqlAuthDao.createAuth(new AuthData("token", "hp"));
        assertFalse(sqlAuthDao.listAuths().isEmpty());
    }
}
