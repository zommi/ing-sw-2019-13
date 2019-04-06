
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
        bullet.getDamage() = 0;
        bullet.getMarks() = 1; //It gives one mark to the visible character that shot me.
        bullet.getPushValue() = 0; //It does not move the character.
        bullet.getPushOrientation() = 0; //As a consequence it does not have a push orientation.
        bullet.getTeleporterFlag() = 0;
    }


    public void usePowerup() {
        // TODO implement here
        return null;
    }

}