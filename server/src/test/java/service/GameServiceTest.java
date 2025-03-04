package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import record.JoinData;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTest {
    private final MemoryGameDAO gameDao = new MemoryGameDAO();
    private final MemoryAuthDAO authDao = new MemoryAuthDAO();
    private final GameService SERVICE = new GameService(authDao, gameDao);

    @BeforeEach
    void clear() {
        gameDao.clearData();
        authDao.clearData();
    }

    @Test
    @Order(1)
    void getGame() throws DataAccessException{
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game5");
        assertNotNull(SERVICE.getGame("game5").game());
    }

    @Test
    @Order(2)
    void createGameTest() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game");
        assertEquals("game", SERVICE.getGame("game").gameName());
        assertNotNull(SERVICE.getGame("game").game());
    }

    @Test
    @Order(3)
    void createGameFail() {
        assertThrows(DataAccessException.class, () -> SERVICE.createGame("token", "game"), "Error: bad request");
    }

    @Test
    @Order(4)
    void joinGameTest() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game");
        GameData game = new GameData(SERVICE.getGame("game").gameID(), "hp", null, "game", SERVICE.getGame("game").game());
        JoinData whiteJoin = new JoinData("WHITE", game.gameID());
        SERVICE.joinGame(auth.authToken(), whiteJoin);
        assertEquals(game, SERVICE.getGame("game"));
    }

    @Test
    @Order(5)
    void joinGameFail() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game");
        JoinData whiteJoin = new JoinData("WHITE", SERVICE.getGame("game").gameID());
        SERVICE.joinGame(auth.authToken(), whiteJoin);
        assertThrows(DataAccessException.class, () -> SERVICE.joinGame(auth.authToken(), whiteJoin), "Error: already taken");
    }

    @Test
    @Order(6)
    void listGames() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game");
        SERVICE.createGame("token", "game2");
        assertNotNull(SERVICE.listGames(auth.authToken()));
    }

    @Test
    @Order(7)
    void listGamesFail() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game");
        assertThrows(DataAccessException.class, () -> SERVICE.createGame("token2", "game2"), "Error: unauthorized");
    }
}
