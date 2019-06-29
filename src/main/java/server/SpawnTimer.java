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
    private int id;

    public SpawnTimer(Controller controller, int id){
        this.controller = controller;
        this.id = id;
    }

    public void run(){
        //System.out.println("Spawn timer task started at: " + new Date());

        //System.out.println("Spawn timer task finished at: " + new Date());

        System.out.println("Spawn timer " + id + " triggered, moving to FIRST_ACTION");

        //spawning not-spawned-yet players
        for(PlayerAbstract playerAbstract : controller.getPlayersToSpawn()){
            playerAbstract.spawn(controller.getCurrentGame().getCurrentGameBoard().getMap().getRandomSpawnpoint());
        }
        controller.getPlayersToSpawn().clear();

        //current phase is spawnphase
        controller.getCurrentGame().getTurnHandler().nextPhase();
    }
}
