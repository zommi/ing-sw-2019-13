package server;

import client.SpawnInfo;
import constants.Constants;
import server.controller.Controller;
import server.controller.playeraction.SpawnAction;
import server.controller.turns.TurnPhase;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;
import view.GameBoardAnswer;
import view.PlayerHandAnswer;
import view.SetSpawnAnswer;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class NextPlayerTimer extends TimerTask {
    private Controller controller;
    private PlayerAbstract playerAbstract;
    private int id;

    public NextPlayerTimer(Controller controller, PlayerAbstract playerAbstract, int id){
        this.controller = controller;
        this.playerAbstract = playerAbstract;
        this.id = id;
    }

    public void run(){
        //System.out.println("Action timer " + id + " task started at: " + new Date());
        //System.out.println("Action timer " + id + " triggered at: " + new Date());

        System.out.println("Action timer " + id + " triggered, moving to END_TURN");

        //this just sets player as disconnected, but socket/rmi connection isn't stopped
        //it won't be taken into account when switching to next player

        System.out.println("Disconnecting " + playerAbstract.getName() + " for inactivity");

        controller.getGameManager().disconnect(playerAbstract);
        if (controller.getGameManager().getActivePlayersNum() < Constants.MIN_PLAYERS_TO_CONTINUE) {
            controller.getGameManager().endGame();
        }
        else {
            if(playerAbstract.getPlayerState() == PlayerState.TOBESPAWNED){
                SpawnInfo spawnInfo = new SpawnInfo(playerAbstract.getRandomPowerupCard());
                SpawnAction spawnAction = new SpawnAction(spawnInfo, playerAbstract, controller.getCurrentGame().getCurrentGameBoard());
                if(controller.getCurrentGame().getTurnHandler().setAndDoAction(spawnAction)) {
                    controller.getGameManager().sendToEverybody(new GameBoardAnswer(controller.getCurrentGame().getCurrentGameBoard()));
                    controller.getGameManager().sendToSpecific(new SetSpawnAnswer(false), playerAbstract.getClientID());
                    controller.getGameManager().sendToSpecific(new PlayerHandAnswer(playerAbstract.getHand()), playerAbstract.getClientID());
                }else{
                    controller.sendErrorMessage(playerAbstract.getClientID());
                }
            }
            controller.getCurrentGame().getTurnHandler().setCurrentPhase(TurnPhase.END_TURN);
            controller.getCurrentGame().getTurnHandler().nextPhase();
        }
    }

}
