package server;

import constants.Constants;
import server.controller.Controller;
import server.model.player.PlayerAbstract;
import view.MessageAnswer;
import view.NoMoreTagbacksAnswer;

import java.util.TimerTask;

public class TagbackTimer extends TimerTask {
    private Controller controller;
    private int id;

    public TagbackTimer(Controller controller, int id){
        this.controller = controller;
        this.id = id;
    }

    public void run(){
        System.out.println("Tagback timer " + id + " triggered, moving to END_TURN");

        //so that the client won't ask for tagbacks anymore
        controller.getGameManager().sendToEverybody(new NoMoreTagbacksAnswer());

        boolean endGame = false;

        for(PlayerAbstract playerAbstract : controller.getCurrentGame().getTurnHandler().getTagbackPlayers()){
            System.out.println("Disconnecting " + playerAbstract.getName() + " for inactivity");

            controller.getGameManager().setInactive(playerAbstract);

            if (controller.getGameManager().getActivePlayers().size() < Constants.MIN_PLAYERS_TO_CONTINUE) {
                endGame = true;
                break;
            }

            //sending message to players
            controller.getGameManager().sendEverybodyExcept(
                    new MessageAnswer(playerAbstract.getName() + " is AFK"), playerAbstract.getClientID());


        }

        if(!endGame)
            controller.getCurrentGame().getTurnHandler().nextPhase();

    }
}
