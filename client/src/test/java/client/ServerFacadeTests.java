package client;

import exception.ResponseException;
import model.GameData;
import record.JoinData;
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

    @Test
    public void clearTest() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        UserData login = new UserData("hp", "password", "");
        facade.login(login);
        assertDoesNotThrow(() -> facade.clear());
    }

    @Test
    public void createGame() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        UserData login = new UserData("hp", "password", "");
        AuthData auth = facade.login(login);
        int id = facade.createGame(auth, new ServerFacade.Game("game"));
        assertEquals(4,Integer.toString(id).length());
    }

    @Test
    public void createGameFail() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        UserData login = new UserData("hp", "password", "");
        facade.login(login);
        AuthData badAuth = new AuthData("badAuth", "hp");
        assertThrows(ResponseException.class,
                () -> facade.createGame(badAuth, new ServerFacade.Game("game")));
    }

    @Test
    public void listGames() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        UserData login = new UserData("hp", "password", "");
        AuthData auth = facade.login(login);
        facade.createGame(auth, new ServerFacade.Game("game"));
        facade.createGame(auth, new ServerFacade.Game("game2"));
        assertEquals(2, facade.listGames(auth).length);
    }

    @Test
    public void listGamesFail() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        UserData login = new UserData("hp", "password", "");
        AuthData auth = facade.login(login);
        AuthData badAuth = new AuthData("badAuth", "hp");
        assertThrows(ResponseException.class,
                () -> facade.listGames(badAuth));
    }

    @Test
    public void updateGame() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        UserData login = new UserData("hp", "password", "");
        AuthData auth = facade.login(login);
        int id = facade.createGame(auth, new ServerFacade.Game("game"));
        assertDoesNotThrow(() -> facade.updateGame(auth, new JoinData("WHITE", id)));
    }

    @Test
    public void updateGameFail() throws ResponseException {
        UserData user = new UserData("hp", "password", "email");
        facade.register(user);
        UserData login = new UserData("hp", "password", "");
        AuthData auth = facade.login(login);
        int id = facade.createGame(auth, new ServerFacade.Game("game"));
        assertThrows(ResponseException.class,
                () -> facade.updateGame(new AuthData("badAuth", "hp"), new JoinData("WHITE", id)));
    }
}

