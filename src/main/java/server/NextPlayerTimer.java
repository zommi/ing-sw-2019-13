package server;

import constants.Constants;
import server.controller.Controller;
import server.controller.turns.TurnPhase;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class NextPlayerTimer extends TimerTask {
    private Controller controller;
    private boolean activated;

    public NextPlayerTimer(Controller controller){
        this.controller = controller;
    }

    public void run(){
        System.out.println("Action timer task started at: " + new Date());
        start();
        System.out.println("Action timer task finished at: " + new Date());

        if(!activated)
            return;

        System.out.println("Timer expired, moving to END_TURN");

        //this just sets player as disconnected, but socket/rmi connection isn't stopped
        //it won't be taken into account when switching to next player
        controller.getCurrentGame().getCurrentPlayer().setConnected(false);
        controller.getGameManager()

        controller.getCurrentGame().getTurnHandler().setCurrentPhase(TurnPhase.END_TURN);
        controller.getCurrentGame().getTurnHandler().nextPhase();
    }

    private void start() {
        try {
            TimeUnit.SECONDS.sleep(Constants.ACTION_TIMEOUT_SEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
