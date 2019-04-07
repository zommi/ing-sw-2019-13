
import java.util.*;


public class Newton implements Powerup {


    private Bullet bullet;
    private boolean isvalid; //This boolean tells me if I can use this powerup card or not, depending on the fact that my target is also receiving damage or he's only receiving marks.


    private int price; //TODO pay one ammocube of any color to use the weapon

    public Newton() {
    }


    public void usePowerup() {
        bullet = new Bullet(0, 0, 1, 0, 0);
        //It does not give more marks.
        //It does not move the character.
        //As a consequence it does not have a push orientation.

        //The user has to choose the target and has to pay one ammocube. I don't know how to represent the cost of the ammocube. We have to
        //study a solution for it. It has to be the same implementation of the weapons cost.
        return null;
    }

}