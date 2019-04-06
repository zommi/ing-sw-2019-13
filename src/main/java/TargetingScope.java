
import java.util.*;

public class TargetingScope implements Powerup {

    // The character can use this card before or after his action. He has to choose another character and move it
    // 1 or 2 squares in one direction.

    private Bullet bullet;

    public TargetingScope() {
        bullet.getDamage() = 0;
        bullet.getMarks() = 0; //It does not give more marks.
        bullet.getTeleporterFlag() = 0;
    }


    public void usePowerup() {
        // TODO implement here
        //It has to prepare the bullet with the push and the pushorientation that the user wants.
        //private int push;  //It can be 1 or 2. Maybe we could use an enum.
        //private char pushOrientation;
        return null;
    }

}