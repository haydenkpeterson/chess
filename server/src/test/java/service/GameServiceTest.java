package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.GameData;
import record.JoinData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private final MemoryGameDAO gameDao = new MemoryGameDAO();
    private final MemoryAuthDAO authDao = new MemoryAuthDAO();
    static final GameService service = new GameService(new MemoryAuthDAO(), new MemoryGameDAO());

    @BeforeEach
    void clear() {
        gameDao.clearData();
    }

    @Test
    void getGame() throws DataAccessException{
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        assertNotNull(service.getGame("game").game());
    }

    @Test
    void createGameTest() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        assertEquals("game", service.getGame("game").gameName());
        assertNotNull(service.getGame("game").game());
    }

    @Test
    void createGameFail() {
        assertThrows(DataAccessException.class, () -> service.createGame("token", "game"), "Error: bad request");
    }

    @Test
    void joinGameTest() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        GameData game = new GameData(service.getGame("game").gameID(), "hp", null, "game", service.getGame("game").game());
        JoinData whiteJoin = new JoinData("WHITE", game.gameID());
        service.joinGame(auth.authToken(), whiteJoin);
        assertEquals(game, service.getGame("game"));
    }

    @Test
    void joinGameFail() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        JoinData whiteJoin = new JoinData("WHITE", service.getGame("game").gameID());
        service.joinGame(auth.authToken(), whiteJoin);
        assertThrows(DataAccessException.class, () -> service.joinGame(auth.authToken(), whiteJoin), "Error: already taken");
    }

    @Test
    void listGames() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        service.createGame("token", "game2");
        assertNotNull(service.listGames(auth.authToken()));
    }

    @Test
    void listGamesFail() throws DataAccessException {
        AuthData auth = new AuthData("token", "hp");
        service.createAuth(auth);
        service.createGame("token", "game");
        assertThrows(DataAccessException.class, () -> service.createGame("token2", "game2"), "Error: unauthorized");
    }
}
