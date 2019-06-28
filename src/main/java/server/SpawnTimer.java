package server;

import constants.Constants;
import server.controller.Controller;
import server.controller.turns.TurnPhase;
import server.model.player.PlayerAbstract;
import view.DisconnectAnswer;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SpawnTimer extends TimerTask {

    private Controller controller;

    public SpawnTimer(Controller controller){
        this.controller = controller;
    }

    public void run(){
        System.out.println("Spawn timer task started at: " + new Date());
        start();
        System.out.println("Spawn timer task finished at: " + new Date());

        if(controller.getCurrentGame().getTurnHandler().getCurrentTimerTask() != this){
            System.out.println("Spawn timer not triggered");
            return;
        }


        System.out.println("Spawn timer expired, moving to FIRST_ACTION");

        //spawning not-spawned-yet players
        for(PlayerAbstract playerAbstract : controller.getPlayersToSpawn()){
            playerAbstract.spawn(controller.getCurrentGame().getCurrentGameBoard().getMap().getRandomSpawnpoint());
        }
        controller.getPlayersToSpawn().clear();

        //current phase is spawnphase
        controller.getCurrentGame().getTurnHandler().nextPhase();
    }

    private void start() {
        try {
            TimeUnit.SECONDS.sleep(Constants.SPAWN_TIMEOUT_SEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
