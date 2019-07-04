package server.controller.playeraction.normalaction;

import server.controller.Controller;

public interface Action {

    /**
     * This method executes an action sent by the client. It checks if the action is valid and if so it actuates it
     * @return an int that indicates if the action has been made
     */
    public boolean execute(Controller controller);
}
