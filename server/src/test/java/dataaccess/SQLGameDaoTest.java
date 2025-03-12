package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import record.JoinData;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDaoTest {
    private final SQLGameDao sqlGameDao = new SQLGameDao();

    @BeforeEach
    void clear() {
        sqlGameDao.clearData();
    }

    @Test
    void createGamePass() throws DataAccessException, SQLException {
        sqlGameDao.createGame(new GameData(1234, null, null, "game1", new ChessGame()));
        assertNotNull(sqlGameDao.getGame("game1"));
    }

    @Test
    void createGameFail() throws DataAccessException, SQLException {
        sqlGameDao.createGame(new GameData(1234, null, null, "game1", new ChessGame()));
        assertThrows(RuntimeException.class, () -> {
            sqlGameDao.createGame(new GameData(1234, null, null, "game1", new ChessGame()));
        });
    }

    @Test
    void listGamesPass() throws DataAccessException, SQLException {
        sqlGameDao.createGame(new GameData(1234, null, null, "game1", new ChessGame()));
        assertNotNull(sqlGameDao.listGames());
    }

    @Test
    void listGamesFail() throws DataAccessException, SQLException {
        assertTrue(sqlGameDao.listGames().isEmpty());
    }

    @Test
    void updateGamePass() throws SQLException, DataAccessException {
        sqlGameDao.createGame(new GameData(12, null, null, "game1", new ChessGame()));
        sqlGameDao.updateGame(new AuthData("token", "hp"), new JoinData("WHITE", 12));
        assertThrows(DataAccessException.class, () -> {
            sqlGameDao.updateGame(new AuthData("token", "hp"), new JoinData("WHITE", 12));
        });
    }

    @Test
    void findGameFromIDPass() throws SQLException, DataAccessException {
        sqlGameDao.createGame(new GameData(12, null, null, "game1", new ChessGame()));
        assertNotNull(sqlGameDao.findGameFromId(12));
    }

    @Test
    void findGameFromIDFail() throws SQLException, DataAccessException {
        assertNull(sqlGameDao.findGameFromId(12));
    }

    @Test
    void getGamePass() throws SQLException, DataAccessException {
        GameData game = new GameData(12, "", "", "game1", new ChessGame());
        sqlGameDao.createGame(game);
        assertEquals(sqlGameDao.getGame("game1").gameName(), game.gameName());
    }

    @Test
    void getGameFail() throws SQLException, DataAccessException {
        assertNull(sqlGameDao.getGame("fakegame"));
    }

    @Test
    void updateGameFail() throws SQLException, DataAccessException {
        sqlGameDao.createGame(new GameData(12, "hp", null, "game1", new ChessGame()));
        sqlGameDao.updateGame(new AuthData("token", "hp"), new JoinData("WHITE", 12));
        assertEquals(new GameData(12, "hp", null, "game1", sqlGameDao.getGame("game1").game()).whiteUsername(), sqlGameDao.getGame("game1").whiteUsername());
    }

    @Test
    void clearData() throws DataAccessException, SQLException {
        sqlGameDao.createGame(new GameData(1234, "", "", "game1", new ChessGame()));
        sqlGameDao.clearData();
        assertTrue(sqlGameDao.listGames().isEmpty());
    }
}
