package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import record.JoinData;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTest {
    private final MemoryGameDAO gameDao = new MemoryGameDAO();
    private final MemoryAuthDAO authDao = new MemoryAuthDAO();
    private final GameService service = new GameService(authDao, gameDao);

    @BeforeEach
    void clear() {
        gameDao.clearData();
        authDao.clearData();
    }

    @Test
    @Order(1)
    void getGame() throws DataAccessException, SQLException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game5");
        assertNotNull(service.getGame("game5").game());
    }

    @Test
    @Order(2)
    void createGameTest() throws DataAccessException, SQLException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        assertEquals("game", service.getGame("game").gameName());
        assertNotNull(service.getGame("game").game());
    }

    @Test
    @Order(3)
    void createGameFail() {
        assertThrows(DataAccessException.class, () -> service.createGame("token", "game"), "Error: bad request");
    }

    @Test
    @Order(4)
    void joinGameTest() throws DataAccessException, SQLException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        GameData game = new GameData(service.getGame("game").gameID(), "hp", null, "game", service.getGame("game").game());
        JoinData whiteJoin = new JoinData("WHITE", game.gameID());
        service.joinGame(auth.authToken(), whiteJoin);
        assertEquals(game, service.getGame("game"));
    }

    @Test
    @Order(5)
    void joinGameFail() throws DataAccessException, SQLException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        JoinData whiteJoin = new JoinData("WHITE", service.getGame("game").gameID());
        service.joinGame(auth.authToken(), whiteJoin);
        assertThrows(DataAccessException.class, () -> service.joinGame(auth.authToken(), whiteJoin), "Error: already taken");
    }

    @Test
    @Order(6)
    void listGames() throws DataAccessException, SQLException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        service.createGame("token", "game2");
        assertNotNull(service.listGames(auth.authToken()));
    }

    @Test
    @Order(7)
    void listGamesFail() throws DataAccessException, SQLException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        assertThrows(DataAccessException.class, () -> service.createGame("token2", "game2"), "Error: unauthorized");
    }
}
