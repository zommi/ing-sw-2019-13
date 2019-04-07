
import java.util.*;

/**
 * 
 */
public class TagbackGrenade implements Powerup {

    //This PowerUp can be used during another's turn. So we can check the value of bullet.getDamage() everytime a player receive a bullet
    //if it is > 0 then this can be used.

    private Bullet bullet;
    private boolean isvalid; //This boolean tells me if I can use this powerup card or not, depending on if I have received a damage from a character I can see.

    public TagbackGrenade() {
    }


    public void usePowerup() {
        // TODO implement here

        bullet = new Bullet(0,0,0,1,0);
        //It gives one mark to the visible character that shot me.
        //It does not move the character.
        return null;
    }

}