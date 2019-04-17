package Player;

import Cards.Weapon;
import Constants.Constants;

import java.util.List;

public class Turn {
    private PlayerAbstract currentPlayer;

    public void setCurrentPlayer(PlayerAbstract currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public PlayerAbstract getCurrentPlayer() {
        return currentPlayer;
    }

    public void playAction(Action action){
        currentPlayer.setAction(action);
        currentPlayer.doAction();
    }

    public void playPowerup(int index, int extra){
        if(extra != Constants.NO_POWERUP)currentPlayer.usePowerup(index,extra);
    }

    public void reloadWeapon(List<Weapon> weaponsToReload){
        for(Weapon w : weaponsToReload){
            w.charge();
        }
    }

    /*
    public void playTurn(TurnInfo info){
        playPowerup(info.get);
        playAction();
        playPowerup();
        playAction();
        playPowerup();

    }
     */
}
