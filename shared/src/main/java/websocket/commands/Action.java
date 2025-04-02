package websocket.commands;

import java.lang.reflect.Type;

public class Action extends UserGameCommand{
    public Action(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }

    public CommandType type() {
        return getCommandType();
    }
}
