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
import server.model.game.GameState;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;
import view.GrenadeAnswer;

import java.util.*;

public class TurnHandler {


    private TurnPhase currentTurnPhase;

    private TurnPhase lastPhase;

    private Controller controller;

    private TimerTask currentTimerTask;

    private Timer timer;

    private int timerId;

    private List<PlayerAbstract> tagbackPlayers;


    public TurnHandler(Controller controller){
        this.controller = controller;
        setCurrentTurnPhase(TurnPhase.FIRST_ACTION);
        tagbackPlayers = new ArrayList<>();
        timerId = 0;
        timer = new Timer(true);
    }

    public TimerTask getCurrentTimerTask() {
        return currentTimerTask;
    }

    public TurnPhase getCurrentTurnPhase(){
        return this.currentTurnPhase;
    }

    public boolean setAndDoAction(Action action){
        boolean actionValid = false;
        if(currentTurnPhase == TurnPhase.FIRST_ACTION || currentTurnPhase == TurnPhase.SECOND_ACTION) {
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
        }else if (currentTurnPhase == TurnPhase.END_TURN && action instanceof ReloadAction){
            actionValid = action.execute(controller);
            if(actionValid){
                nextPhase();
            }
        }
        return actionValid;
    }

    public boolean setAndDoSpawn(Action action){
        return currentTurnPhase == TurnPhase.RESPAWN_PHASE && action.execute(controller);
    }

    public void setCurrentTurnPhase(TurnPhase currentTurnPhase) {
        this.currentTurnPhase = currentTurnPhase;
        try {
            controller.getCurrentGame().getCurrentGameBoard().setCurrentTurnPhase(currentTurnPhase);
        }catch(Exception e){
            //do nothing
        }
    }

    public void startTagbackPhase(){
        //canceling current timer
        currentTimerTask.cancel();

        lastPhase = currentTurnPhase;
        setCurrentTurnPhase(TurnPhase.TAGBACK_PHASE);

        //sending everybody new phase
        controller.sendEverybodyGameboardAndHand();

        for(PlayerAbstract playerAbstract : tagbackPlayers){
            controller.getGameManager().sendToSpecific(new GrenadeAnswer(), playerAbstract.getClientID());
        }

        startTagbackTimer();
    }

    public void nextPhase(){
        switch (currentTurnPhase){
            case FIRST_ACTION:
                //stopping first action timer
                currentTimerTask.cancel();

                if(controller.getCurrentGame().getCurrentState() == GameState.NORMAL) {
                    System.out.println("First action done, moving to second action");
                    setCurrentTurnPhase(TurnPhase.SECOND_ACTION);
                }else{
                    setCurrentTurnPhase(controller.getCurrentGame().getCurrentPlayer().getPlayerState() == PlayerState.BEFORE_FIRST_PLAYER_FF ?
                            TurnPhase.SECOND_ACTION :
                            TurnPhase.END_TURN);
                }
                //starting second action timer
                startNextPlayerTimer();

                break;

            case SECOND_ACTION:
                //stopping second action timer
                currentTimerTask.cancel();

                System.out.println("Second action done, moving to next phase");
                setCurrentTurnPhase(TurnPhase.END_TURN);

                //starting reload timer
                startNextPlayerTimer();

                break;

            case END_TURN:
                //stopping reload timer, but also first and second action if they have been triggered(no problem)
                currentTimerTask.cancel();

                controller.restoreSquares();
                System.out.println("restored squares");

                //checking if someone died
                boolean someoneDied = false;
                for(PlayerAbstract playerAbstract : controller.getCurrentGame().getActivePlayers()) {
                    if (playerAbstract.getPlayerBoard().getDamageTaken() > Constants.DEATH_THRESHOLD) {
                        someoneDied = true;
                    }
                }

                if(someoneDied){
                    System.out.println("Someone died, moving to RESPAWN_PHASE");
                    setCurrentTurnPhase(TurnPhase.RESPAWN_PHASE);

                    //starting spawn timer
                    startSpawnTimer();
                    controller.handleDeaths();
                }else {
                    controller.setCurrentID(controller.getCurrentGame().nextPlayer());

                    if(controller.getCurrentGame().getCurrentPlayer().equals(controller.getFirstFrenzyPlayer()))
                        controller.getGameManager().endGame();

                    System.out.println("changed player in game, moving from endturn to firstAction");
                    controller.sendChangeCurrentPlayer();
                    setCurrentTurnPhase(TurnPhase.FIRST_ACTION);

                    //starting first action timer
                    startNextPlayerTimer();
                }
                break;
            case RESPAWN_PHASE:
                //stopping spawn timer
                currentTimerTask.cancel();

                controller.setCurrentID(controller.getCurrentGame().nextPlayer());

                if(controller.getCurrentGame().getCurrentPlayer().equals(controller.getFirstFrenzyPlayer()))
                    controller.getGameManager().endGame();

                System.out.println("changed player in game");
                controller.sendChangeCurrentPlayer();

                setCurrentTurnPhase(TurnPhase.FIRST_ACTION);

                if(this.controller.isFinalFrenzy() && controller.getCurrentGame().getCurrentState() != GameState.FINAL_FRENZY){
                    this.controller.startFrenzy();
                }
                //starting first action timer
                startNextPlayerTimer();
                break;
            case TAGBACK_PHASE:
                //stopping tagback timer
                currentTimerTask.cancel();

                setCurrentTurnPhase(lastPhase);

                nextPhase();

                break;
            default: break;
        }
        controller.sendEverybodyGameboardAndHand();
    }

    public void startNextPlayerTimer(){
        currentTimerTask = new NextPlayerTimer(controller, controller.getCurrentGame().getCurrentPlayer(), timerId);
        timer.schedule(currentTimerTask, Constants.ACTION_TIMEOUT_MSEC);
        timerId++;
    }

    private void startSpawnTimer(){
        currentTimerTask = new SpawnTimer(controller, timerId);
        timer.schedule(currentTimerTask, Constants.SPAWN_TIMEOUT_MSEC);
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
        System.out.println("Client " + clientID + " stopped playing tagbacks");
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
