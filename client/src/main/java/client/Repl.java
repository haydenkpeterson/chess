package client;

import java.util.Scanner;
import static ui.EscapeSequences.*;
import client.websocket.NotificationHandler;
import com.google.gson.Gson;
import model.GameData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class Repl implements NotificationHandler{
    private final Client client;

    public Repl(String serverUrl) {
        client = new Client(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to 240 chess. Type help to get started.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(ServerMessage message) {
        if(message instanceof LoadGameMessage loadGameMessage) {
            GameData gameData = loadGameMessage.getGame();
            client.setStoredGame(gameData);
            System.out.println(client.redraw());
        }
        else {
            System.out.println(SET_TEXT_COLOR_RED + message);
            printPrompt();
        }
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_BG_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
