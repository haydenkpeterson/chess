package websocket.commands;

public class Action extends UserGameCommand{
    public Action(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }
}
