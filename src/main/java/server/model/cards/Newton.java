package server.model.cards;

import java.util.Scanner;

public class Newton extends Powerup {

    // The character can use this card before or after his action. He has to choose another character and move it
    // 1 or 2 squares in one direction.

    private Bullet bullet;

    public Newton() {
    }


    public Bullet usePowerup() {
        // TODO implement here
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a coordinate x you want to move the target: ");
        int x = reader.nextInt(); // Scans the next token of the input as an int.
        System.out.println("Enter a coordinate y you want to move the target: ");
        int y = reader.nextInt();
        reader.close();

        //x and y however needs to be at a maximum of 2 square away HOW CAN I DO IT?

        bullet = new Bullet(x, y, 0, 0, false);

        return bullet;
    }

    @Override
    public String getName() {
        return "Newton";
    }
}