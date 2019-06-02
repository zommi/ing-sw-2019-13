package server.controller.turns;

import client.CollectInfo;
import client.MoveInfo;
import exceptions.WrongGameStateException;
import server.Server;
import server.controller.Controller;
import server.controller.playeraction.Action;
import server.controller.playeraction.ShootInfo;
import server.controller.playeraction.normalaction.CollectAction;
import server.controller.playeraction.normalaction.MoveAction;
import server.controller.playeraction.normalaction.ShootAction;
import server.model.player.ConcretePlayer;
import server.model.player.PlayerAbstract;
import view.ChangeCurrentPlayerAnswer;

public class TurnHandler {

    private PlayerAbstract currentPlayer;

    private TurnPhase currentPhase;

    private Controller controller;

    private Action action;

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

    public void setAndDoAction(Action action){
        if(currentPhase == TurnPhase.FIRST_ACTION
                || currentPhase == TurnPhase.SECOND_ACTION) {
            this.action = action;
            //if returns false then disconnects the player
            this.action.execute();
            if((action instanceof ShootAction) || (action instanceof CollectAction) || (action instanceof MoveAction)) //if it is a draw or a spawn it is not counted as an action
                nextPhase();
        }
    }

    public void pass(){
        if(currentPhase == TurnPhase.END_TURN){

        }
    }

    public void nextPhase(){
        switch (currentPhase){
            case FIRST_ACTION:
                this.currentPhase = TurnPhase.SECOND_ACTION;
                break;
            case SECOND_ACTION:
                this.currentPhase = TurnPhase.END_TURN;
                break;
            case END_TURN:
                try{
                    ((ConcretePlayer)currentPlayer).getCurrentGame().nextPlayer();
                    controller.sendChangeCurrentPlayer();
                }
                catch(WrongGameStateException e){
                    e.printStackTrace();
                }
                this.currentPhase = TurnPhase.FIRST_ACTION;
                break;
            default: break;
        }
    }



}
