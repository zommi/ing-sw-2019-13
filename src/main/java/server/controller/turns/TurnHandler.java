package server.controller.turns;

import constants.Constants;
import server.NextPlayerTimer;
import server.SpawnTimer;
import server.TagbackTimer;
import server.controller.Controller;
import server.controller.playeraction.*;
import server.controller.playeraction.normalaction.CollectAction;
import server.controller.playeraction.normalaction.MoveAction;
import server.controller.playeraction.normalaction.ShootAction;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerHand;
import view.GrenadeAnswer;

import java.util.*;

public class TurnHandler {


    private TurnPhase currentPhase;

    private TurnPhase lastPhase;

    private Controller controller;

    private TimerTask currentTimerTask;

    private Timer timer;

    private int timerId;

    private List<PlayerAbstract> tagbackPlayers;


    public TurnHandler(Controller controller){
        this.controller = controller;
        this.currentPhase = TurnPhase.FIRST_ACTION;
        tagbackPlayers = new ArrayList<>();
        timerId = 0;
        timer = new Timer(true);
    }

    public TurnPhase getCurrentPhase(){
        return this.currentPhase;
    }

    public boolean setAndDoAction(Action action){
        boolean actionValid = false;
        if(currentPhase == TurnPhase.FIRST_ACTION || currentPhase == TurnPhase.SECOND_ACTION) {
            actionValid = action.execute(controller);
            if(actionValid && (action instanceof CollectAction || action instanceof MoveAction)) {
                //if it is a draw or a spawn it is not counted as an action
                nextPhase();
            }else if(actionValid && action instanceof ShootAction){
                //checking if someone of the damaged player has a tagback grenade
                tagbackPlayers.clear();
                for(PlayerAbstract playerAbstract : controller.getCurrentGame().getActivePlayers()){
                    if(playerAbstract.canPlayTagback(controller.getCurrentGame().getCurrentPlayer())){
                        tagbackPlayers.add(playerAbstract);
                    }
                }
                if(!tagbackPlayers.isEmpty())
                    startTagbackPhase();
                else
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

    public void startTagbackPhase(){
        //canceling current timer
        currentTimerTask.cancel();

        lastPhase = currentPhase;
        currentPhase = TurnPhase.TAGBACK_PHASE;

        for(PlayerAbstract playerAbstract : tagbackPlayers){
            controller.getGameManager().sendToSpecific(new GrenadeAnswer(), playerAbstract.getClientID());
        }

        startTagbackTimer();
    }

    public void nextPhase(){
        switch (currentPhase){
            case FIRST_ACTION:
                //stopping first action timer
                currentTimerTask.cancel();

                System.out.println("First action done, moving to second action");
                currentPhase = TurnPhase.SECOND_ACTION;

                //starting second action timer
                startNextPlayerTimer();

                break;

            case SECOND_ACTION:
                //stopping second action timer
                currentTimerTask.cancel();

                System.out.println("Second action done, moving to next phase");
                currentPhase = TurnPhase.END_TURN;

                //starting reload timer
                startNextPlayerTimer();

                break;

            case END_TURN:
                //stopping reload timer, but also first and second action if they have been triggered(no problem)
                currentTimerTask.cancel();

                controller.restoreSquares();
                System.out.println("restored squares");
                controller.sendSquaresRestored();

                //checking if someone died
                boolean someoneDied = false;
                for(PlayerAbstract playerAbstract : controller.getCurrentGame().getActivePlayers()) {
                    if (playerAbstract.getPlayerBoard().getDamageTaken() > Constants.DEATH_THRESHOLD) {
                        someoneDied = true;
                    }
                }

                if(someoneDied){
                    currentPhase = TurnPhase.SPAWN_PHASE;

                    //starting spawn timer
                    startSpawnTimer();
                    controller.handleDeaths();
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
                currentTimerTask.cancel();

                controller.setCurrentID(controller.getCurrentGame().nextPlayer());
                System.out.println("changed player in game");
                controller.sendChangeCurrentPlayer();
                currentPhase = TurnPhase.FIRST_ACTION;

                //starting first action timer
                startNextPlayerTimer();
                break;
            case TAGBACK_PHASE:
                //stopping tagback timer
                currentTimerTask.cancel();

                currentPhase = lastPhase;

                nextPhase();

                break;
            default: break;
        }
    }

    public void startNextPlayerTimer(){
        currentTimerTask = new NextPlayerTimer(controller, controller.getCurrentGame().getCurrentPlayer(), timerId);
        timer.schedule(currentTimerTask, Constants.ACTION_TIMEOUT_MSEC);
        timerId++;
    }

    private void startSpawnTimer(){
        currentTimerTask = new SpawnTimer(controller, timerId);
        timer.schedule(currentTimerTask, Constants.SPAWN_TIMEOUT_SEC);
        timerId++;
    }

    private void startTagbackTimer() {
        currentTimerTask = new TagbackTimer(controller, timerId);
        timer.schedule(currentTimerTask, Constants.TAGBACK_TIMEOUT_MSEC);
        timerId++;
    }

    public List<PlayerAbstract> getTagbackPlayers() {
        return tagbackPlayers;
    }

    public boolean setAndDoTagback(PowerUpAction powerUpAction) {
        return powerUpAction.execute(controller);
    }

    public void tagbackStop(int clientID) {
        System.out.println("Client " + clientID + "stopped playing tagbacks");
        //removing player
        Iterator<PlayerAbstract> iterator = tagbackPlayers.iterator();
        PlayerAbstract playerAbstract;
        while(iterator.hasNext()){
            playerAbstract = iterator.next();
            if(playerAbstract.getClientID() == clientID)
                iterator.remove();
        }

        //checking if no more players are expected to play a tagback
        if(tagbackPlayers.isEmpty()){
            System.out.println("Exiting from TAGBACK_PHASE");
            nextPhase();
        }
    }
}
