package Player;

import Exceptions.InvalidMoveException;
import Player.PlayerState;

/**
 * 
 */
public class NormalPlayerState extends PlayerState {
    ConcretePlayer player;

    public NormalPlayerState(ConcretePlayer player) {
        super(player);
        this.player = player;
    }

    /*
    public void shoot(int weaponIndex) {
        try {
            hand.playCard(weaponIndex,'w');
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }
    */

    public void collect(){

    }

}