package server.controller.turns;

import exceptions.WrongGameStateException;
import server.controller.Controller;
import server.controller.playeraction.*;
import server.controller.playeraction.normalaction.CollectAction;
import server.controller.playeraction.normalaction.MoveAction;
import server.controller.playeraction.normalaction.ShootAction;
import server.model.player.PlayerAbstract;

public class TurnHandler {

    private PlayerAbstract currentPlayer;

    private TurnPhase currentPhase;

    private Controller controller;



    public void setCurrentPlayer(PlayerAbstract currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void playPowerup(int index){
        currentPlayer.usePowerup(index);
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    /*
    public void reloadWeapon(List<Weapon> weaponsToReload) throws InvalidMoveException{
        if(this.currentPhase == TurnPhase.END_TURN) {
            for (Weapon w : weaponsToReload) {
                w.charge();
            }
        } throw new InvalidMoveException();
    }
    */

    public TurnHandler(){
        this.currentPhase = TurnPhase.FIRST_ACTION;
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

    public void setAndDoSpawn(Action action){
        if(currentPhase == TurnPhase.SPAWN_PHASE){
            boolean actionValid = action.execute(controller);
            if(actionValid && action instanceof SpawnAction){

            }else{
                //mandare errore
            }
        }
    }

    public void pass(){
        if(currentPhase == TurnPhase.END_TURN){

        }
    }

    public void nextPhase(){
        switch (currentPhase){
            case FIRST_ACTION:
                System.out.println("First action done, moving to second action");
                currentPhase = TurnPhase.SECOND_ACTION;
                break;
            case SECOND_ACTION:
                System.out.println("Second action done, moving to next phase");
                currentPhase = TurnPhase.END_TURN;
                break;
            //case POWERUP_TURN:
            //    this.currentPhase = TurnPhase.END_TURN;
            //    break;
            case END_TURN:
                controller.restoreSquares();
                System.out.println("restored squares");
                controller.sendSquaresRestored();
                if(controller.handleDeaths()){
                    controller.sendSpawnRequest();
                    currentPhase = TurnPhase.SPAWN_PHASE;
                }else {
                    try {
                        controller.setCurrentID(controller.getCurrentGame().nextPlayer());
                        System.out.println("changed player in game");
                        controller.sendChangeCurrentPlayer();
                    } catch (WrongGameStateException e) {
                        e.printStackTrace();
                    }
                    currentPhase = TurnPhase.FIRST_ACTION;
                }
                break;
            case SPAWN_PHASE:
                try {
                    controller.setCurrentID(controller.getCurrentGame().nextPlayer());
                    System.out.println("changed player in game");
                    controller.sendChangeCurrentPlayer();
                } catch (WrongGameStateException e) {
                    e.printStackTrace();
                }
                currentPhase = TurnPhase.FIRST_ACTION;
                break;
            default: break;
        }
    }



}
