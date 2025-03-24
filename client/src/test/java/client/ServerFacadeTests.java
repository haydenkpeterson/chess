package client;

import exception.ResponseException;
import serverfacade.ServerFacade;
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
        facade = new ServerFacade("http://localhost:" + String.valueOf(port));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() throws ResponseException {facade.clear();}

    @Test
    public void register() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        AuthData response = facade.register(user);
        assertTrue(response.authToken().length() > 10);
        assertEquals("hp", response.username());
    }

    @Test
    public void registerFail() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        assertThrows(ResponseException.class,
                () -> facade.register(user));
    }

    @Test
    public void login() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        UserData login = new UserData("hp", "password", "");
        AuthData response = facade.login(login);
        assertTrue(response.authToken().length() > 10);
        assertEquals("hp", response.username());
    }

    @Test
    public void loginFail() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        UserData login = new UserData("pp", "password", "");
        assertThrows(ResponseException.class,
                () -> facade.login(login));
    }

    @Test
    public void logout() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        UserData login = new UserData("hp", "password", "");
        AuthData auth = facade.login(login);
        assertDoesNotThrow(() -> facade.logout(auth));
    }

    @Test
    public void logoutFail() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        assertThrows(ResponseException.class,
                () -> facade.logout(new AuthData("auth", "hp")));
    }
}

