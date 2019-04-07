import java.util.Scanner;

public class TargetingScope implements Powerup {

    // The character can use this card before or after his action. He has to choose another character and move it
    // 1 or 2 squares in one direction.

    private Bullet bullet;

    public TargetingScope() {
        bullet = new Bullet(0, 0, 0, 0, 0);
        //it has to ask the user how many squares and in which direction
    }


    public void usePowerup() {
        // TODO implement here

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a coordinate x you want to move the target: ");
        int x = reader.nextInt(); // Scans the next token of the input as an int.
        System.out.println("Enter a coordinate y you want to move the target: ");
        int y = reader.nextInt();
        reader.close();

        bullet = new Bullet(x, y, 0, 0, 0);

        return null;
    }

}