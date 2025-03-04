package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.GameData;
import record.JoinData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private final MemoryGameDAO gameDao = new MemoryGameDAO();
    private final MemoryAuthDAO authDao = new MemoryAuthDAO();
    static final GameService SERVICE = new GameService(new MemoryAuthDAO(), new MemoryGameDAO());

    @BeforeEach
    void clear() {
        gameDao.clearData();
    }

    @Test
    void getGame() throws DataAccessException{
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game");
        assertNotNull(SERVICE.getGame("game").game());
    }

    @Test
    void createGameTest() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game");
        assertEquals("game", SERVICE.getGame("game").gameName());
        assertNotNull(SERVICE.getGame("game").game());
    }

    @Test
    void createGameFail() {
        assertThrows(DataAccessException.class, () -> SERVICE.createGame("token", "game"), "Error: bad request");
    }

    @Test
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
    void joinGameFail() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game");
        JoinData whiteJoin = new JoinData("WHITE", SERVICE.getGame("game").gameID());
        SERVICE.joinGame(auth.authToken(), whiteJoin);
        assertThrows(DataAccessException.class, () -> SERVICE.joinGame(auth.authToken(), whiteJoin), "Error: already taken");
    }

    @Test
    void listGames() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game");
        SERVICE.createGame("token", "game2");
        assertNotNull(SERVICE.listGames(auth.authToken()));
    }

    @Test
    void listGamesFail() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        SERVICE.createAuth(auth);
        SERVICE.createGame("token", "game");
        assertThrows(DataAccessException.class, () -> SERVICE.createGame("token2", "game2"), "Error: unauthorized");
    }
}
