package client;

import main.java.serverfacade.ResponseException;
import main.java.serverfacade.ServerFacade;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    private static ServerFacade facade;
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(String.valueOf(port));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void register() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        AuthData response = facade.register(user);
        assertTrue(response.authToken().length() > 10);
        assertEquals("hp", response.username());
    }
}
