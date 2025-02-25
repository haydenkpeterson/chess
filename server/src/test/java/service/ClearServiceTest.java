package service;

import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;

import model.AuthData;
import model.UserData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    static final ClearService service = new ClearService(new MemoryUserDAO(), new MemoryGameDAO(), new MemoryAuthDAO());

    @Test
    void deleteAllData() throws ResponseException {


        service.clear();
        assertEquals(0, service.);
    }
}
