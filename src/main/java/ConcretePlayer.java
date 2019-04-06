
import java.util.*;

/**
 * 
 */
public class ConcretePlayer extends PlayerAbstract {

    private String name;
    private Character character;
    private PlayerHand hand;
    private PlayerBoard board;
    /**
     * Default constructor
     */
    public ConcretePlayer(String name) {
        this.name = name;
        this.character = chooseCharacter();
        hand = new PlayerHand();
        board = new PlayerBoard();
    }


    public void move() {
        Scanner sc = new Scanner(System.in);
        String move;
        Square currentPos = character.getPosition();
        Square nextSquare = currentPos;
        List<Square> possibleMoves;
        boolean valid = false;
        for (int i = 0; i < 3; i++) {
            possibleMoves = currentPos.getAdjacentSquares();
            do {
                System.out.println("In which direction do you wish to move? (N/S/W/E)");
                move = sc.nextLine();
                if (move.equals("N")) {
                    valid = possibleMoves.contains(currentPos.getnSquare());
                    nextSquare = currentPos.getnSquare();
                } else if (move.equals("S")) {
                    valid = possibleMoves.contains(currentPos.getsSquare());
                    nextSquare = currentPos.getsSquare();
                } else if (move.equals("W")) {
                    valid = possibleMoves.contains(currentPos.getwSquare());
                    nextSquare = currentPos.getwSquare();
                } else if (move.equals("E")) {
                    valid = possibleMoves.contains(currentPos.geteSquare());
                    nextSquare = currentPos.geteSquare();
                }
            } while (!valid);
            character.move(nextSquare);
            currentPos = nextSquare;
            if (i < 2) {
                System.out.println("Do you wish to move again? (Type 'Yes' if you do)");
                move = sc.nextLine();
                if (!move.equals(("Yes"))) {
                    break;
                }
            }
        }
    }

    public void shoot() {
        if(hand.getWeapons().size() > 0) {
            hand.playCard();
        } else {
            //implement a try catch
            System.out.println("No cards to use!");
        }
    }

    /**
     * @return
     */
    public void collect() {

    }

    /**
     * @return
     */
    public PlayerState getPlayerState() {
        return null;
    }
}









}