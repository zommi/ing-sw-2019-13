import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.*;


public class TargetingScope implements Powerup {


    private Bullet bullet;


    private int price; //TODO pay one ammocube of any color to use the weapon

    public TargetingScope() {
    }


    public Bullet usePowerup() {

        ArrayList<AmmoCube> cubecost = new ArrayList<AmmoCube>();
        AmmoCube e = new AmmoCube(6); //It is colour undefined.
        cubecost.add(e);

        bullet = new Bullet(0, 0, 1, 0, false, cubecost);
        //It does not give more marks.
        //It does not move the character.
        //As a consequence it does not have a push orientation.

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the target you shot and that you want to use this powerup against: ");
        //Due to the fact that the player knows if he can use the powerup against him or not, the Newton is not going to check

        //The user has to choose the target and has to pay one ammocube. I don't know how to represent the cost of the ammocube. We have to
        //study a solution for it. It has to be the same implementation of the weapons cost.
        return bullet;
    }

}