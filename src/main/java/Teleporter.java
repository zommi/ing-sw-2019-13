
import java.util.*;

/**
 * 
 */
public class Teleporter implements Powerup {

    private Bullet bullet;

    //This one does not affect the opponents, it only affects the character that is using it.
    public Teleporter() {
        bullet.getDamage() = 0;
        bullet.getMarks() = 0; //It gives one mark to the visible character that shot me.
        bullet.getPushValue() = 0; //It does not move the character.
        bullet.getPushOrientation() = 0; //As a consequence it does not have a push orientation.
        bullet.getTeleporterFlag() = 1;
    }


    public void usePowerup() {
        // TODO implement here
        return null;
    }

}