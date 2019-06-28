package server.controller.turns;

import server.NextPlayerTimer;
import server.SpawnTimer;
import server.controller.Controller;
import server.controller.playeraction.*;
import server.controller.playeraction.normalaction.CollectAction;
import server.controller.playeraction.normalaction.MoveAction;
import server.controller.playeraction.normalaction.ShootAction;

import java.util.Timer;
import java.util.TimerTask;

public class TurnHandler {


    private TurnPhase currentPhase;

    private Controller controller;

    private TimerTask currentTimerTask;

    private Timer timer;

    /*
    public void reloadWeapon(List<Weapon> weaponsToReload) throws InvalidMoveException{
        if(this.currentPhase == TurnPhase.END_TURN) {
            for (Weapon w : weaponsToReload) {
                w.charge();
            }
        } throw new InvalidMoveException();
    }
    */

    public TurnHandler(Controller controller){
        this.controller = controller;
        this.currentPhase = TurnPhase.FIRST_ACTION;
        timer = new Timer(true);
    }

    public TurnPhase getCurrentPhase(){
        return this.currentPhase;
    }

    public boolean setAndDoAction(Action action){
        boolean actionValid = false;
        if(currentPhase == TurnPhase.FIRST_ACTION || currentPhase == TurnPhase.SECOND_ACTION) {
            actionValid = action.execute(controller);
            if(actionValid &&
                    ((action instanceof ShootAction) ||
                    (action instanceof CollectAction) ||
                    (action instanceof MoveAction))) {//if it is a draw or a spawn it is not counted as an action
                nextPhase();
            }
        }else if (currentPhase == TurnPhase.END_TURN && action instanceof ReloadAction){
            actionValid = action.execute(controller);
            if(actionValid){
                nextPhase();
            }
        }
        return actionValid;
    }

    public boolean setAndDoSpawn(Action action){
        return currentPhase == TurnPhase.SPAWN_PHASE && action.execute(controller);
    }

    public void setCurrentPhase(TurnPhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public void nextPhase(){
        switch (currentPhase){
            case FIRST_ACTION:
                //stopping first action timer
                currentTimerTask = null;

                System.out.println("First action done, moving to second action");
                currentPhase = TurnPhase.SECOND_ACTION;

                //starting second action timer
                startNextPlayerTimer();

                break;

            case SECOND_ACTION:
                //stopping second action timer
                currentTimerTask = null;


                System.out.println("Second action done, moving to next phase");
                currentPhase = TurnPhase.END_TURN;

                //starting reload timer
                startNextPlayerTimer();

                break;

            case END_TURN:
                //stopping reload timer, but also first and second action if they have been triggered(no problem)
                currentTimerTask = null;

                controller.restoreSquares();
                System.out.println("restored squares");
                controller.sendSquaresRestored();
                if(controller.handleDeaths()){
                    currentPhase = TurnPhase.SPAWN_PHASE;
                    controller.sendSpawnRequest();

                    //starting spawn timer
                    startSpawnTimer();
                }else {
                    controller.setCurrentID(controller.getCurrentGame().nextPlayer());
                    System.out.println("changed player in game, moving from endturn to firstAction");
                    controller.sendChangeCurrentPlayer();
                    currentPhase = TurnPhase.FIRST_ACTION;

                    //starting first action timer
                    startNextPlayerTimer();
                }
                break;
            case SPAWN_PHASE:
                //stopping spawn timer
                currentTimerTask = null;

                controller.setCurrentID(controller.getCurrentGame().nextPlayer());
                System.out.println("changed player in game");
                controller.sendChangeCurrentPlayer();
                currentPhase = TurnPhase.FIRST_ACTION;

                //starting first action timer
                startNextPlayerTimer();
                break;
            default: break;
        }
    }

    public void startNextPlayerTimer(){
        currentTimerTask = new NextPlayerTimer(controller, controller.getCurrentGame().getCurrentPlayer());
        timer.schedule(currentTimerTask, 0);
    }

    public void startSpawnTimer(){
        currentTimerTask = new SpawnTimer(controller);
        timer.schedule(currentTimerTask, 0);
    }

    public TimerTask getCurrentTimerTask() {
        return currentTimerTask;
    }
}
