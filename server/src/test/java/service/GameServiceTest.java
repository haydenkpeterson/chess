package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private final MemoryGameDAO gameDao = new MemoryGameDAO();
    static final GameService service = new GameService(new MemoryGameDAO());

    @BeforeEach
    void clear() {
        gameDao.clearData();
    }

    @Test
    void getGame() throws DataAccessException{
        service.createGame("game");
        assertNotNull(service.getGame("game").game());
    }

    @Test
    void createGameTest() throws DataAccessException {
        service.createGame("game");
        assertEquals("game", service.getGame("game").gameName());
        assertNotNull(service.getGame("game").game());
    }

    @Test
    void createGameFail() {
        assertThrows(DataAccessException.class, () -> service.createGame(null), "Error: bad request");
    }

    @Test
    void joinGameTest() throws DataAccessException {
        service.createGame("game");
        GameData game = new GameData(service.getGame("game").gameID(), "white", "black", "game", service.getGame("game").game());
        GameData whiteJoin = new GameData(service.getGame("game").gameID(), "white", null, null, null);
        GameData blackJoin = new GameData(service.getGame("game").gameID(), null, "black", null, null);
        service.joinGame(whiteJoin);
        service.joinGame(blackJoin);
        assertEquals(game, service.getGame("game"));
    }

    @Test
    void joinGameFail() throws DataAccessException {
        service.createGame("game");
        GameData whiteJoin = new GameData(service.getGame("game").gameID(), "white", null, null, null);
        service.joinGame(whiteJoin);
        assertThrows(DataAccessException.class, () -> service.joinGame(whiteJoin), "Error: already taken");
    }

    @Test
    void listGames() throws DataAccessException {
        service.createGame("game");
        service.createGame("game1");
        service.createGame("game2");
        assertFalse(service.listGames().isEmpty());
        assertEquals(3, service.listGames().size());
    }

    @Test
    void listGamesFail() throws DataAccessException {
        service.createGame("game");
        service.createGame("game1");
        service.createGame("game2");
        assertFalse(service.listGames().isEmpty());
        assertEquals(3, service.listGames().size());
    }
}
