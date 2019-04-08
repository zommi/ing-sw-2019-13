package Cards;

import java.util.Scanner;

/**
 * 
 */
public class Teleporter implements Powerup {

    private Bullet bullet;

    //This one does not affect the opponents, it only affects the character that is using it.
    public Teleporter() {
    }


    public Bullet usePowerup() {
        // TODO implement here
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a coordinate x you want to move: ");
        int x = reader.nextInt(); // Scans the next token of the input as an int.
        System.out.println("Enter a coordinate y you want to move: ");
        int y = reader.nextInt();
        reader.close();

        bullet = new Bullet(x, y, 0, 0, true, null);
        return bullet;
    }

}