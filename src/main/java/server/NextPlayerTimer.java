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

    public NextPlayerTimer(Controller controller, PlayerAbstract playerAbstract){
        this.controller = controller;
        this.playerAbstract = playerAbstract;
    }

    public void run(){
        System.out.println("Action timer task started at: " + new Date());
        start();
        System.out.println("Action timer task finished at: " + new Date());

        if(controller.getCurrentGame().getTurnHandler().getCurrentTimerTask() != this) {
            System.out.println("Timer not triggered");
            return;
        }

        System.out.println("Timer expired, moving to END_TURN");

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

    private void start() {
        try {
            TimeUnit.SECONDS.sleep(Constants.ACTION_TIMEOUT_SEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
