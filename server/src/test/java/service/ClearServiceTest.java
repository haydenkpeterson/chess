package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private final MemoryUserDAO userDAO = new MemoryUserDAO();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static final ClearService SERVICE = new ClearService(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());

    @BeforeEach
    void clear() {
        SERVICE.clear();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO.createUser(new UserData("hp", "deeznuts", "pp@gmail.com"));
        userDAO.createUser(new UserData("yourmom", "password", "minecraft@gmail.com"));
        gameDAO.createGame(new GameData(1123, "yourmom", "hp", "game1", new ChessGame()));
        authDAO.createAuth(new AuthData("token", "hp"));
        authDAO.createAuth(new AuthData("token1", "yourmom"));
    }

    @Test
    void deleteAllData() {
        SERVICE.clear();
        assertTrue(SERVICE.checkClear());
    }
}
