package server.controller.turns;

import exceptions.InvalidMoveException;
import server.controller.playeraction.Action;
import server.controller.playeraction.normalaction.CollectAction;
import server.model.cards.Weapon;
import server.model.player.PlayerAbstract;

import java.util.List;

public class TurnHandler {

    private PlayerAbstract currentPlayer;

    private TurnPhase currentPhase;

    private Action action;

    public void setCurrentPlayer(PlayerAbstract currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public PlayerAbstract getCurrentPlayer() {
        return currentPlayer;
    }

    public void playPowerup(int index){
        currentPlayer.usePowerup(index);
    }

    public void reloadWeapon(List<Weapon> weaponsToReload) throws InvalidMoveException{
        if(this.currentPhase == TurnPhase.END_TURN) {
            for (Weapon w : weaponsToReload) {
                w.charge();
            }
        } throw new InvalidMoveException();
    }

    public void setAction(Action action){
        if(currentPhase == TurnPhase.FIRST_ACTION
            || currentPhase == TurnPhase.SECOND_ACTION){
            this.action = action;
        }
    }

    public void doAction(){
        if(currentPhase == TurnPhase.FIRST_ACTION
                || currentPhase == TurnPhase.SECOND_ACTION) {
            //if returns false then disconnects the player
            this.action.execute();
            nextPhase();
        }
    }

    public void startTurn(PlayerAbstract player){
        this.currentPlayer = player;
        this.currentPhase = TurnPhase.FIRST_ACTION;
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
                this.currentPhase = TurnPhase.FIRST_ACTION;
                break;
            default: break;
        }
    }



}
