package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
