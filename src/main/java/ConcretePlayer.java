
import java.util.*;

/**
 * 
 */
public class ConcretePlayer extends PlayerAbstract {

    private String name;
    private Character character;
    private PlayerHand hand;
    private PlayerBoard board;
    private GameBoard currentGameBoard;

    /**
     * Default constructor
     */
    public ConcretePlayer(String name, GameBoard gameBoard) {
        this.name = name;
        this.character = chooseCharacter();
        this.hand = new PlayerHand(this);
        this.board = new PlayerBoard(this);
        this.currentGameBoard = gameBoard;
    }


    public void move() {
        Scanner sc = new Scanner(System.in);
        String move;
        SquareAbstract currentPos = character.getPosition();
        SquareAbstract nextSquare = currentPos;
        List<SquareAbstract> possibleMoves;
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
        Scanner sc = new Scanner(System.in);
        int choice;
        if(hand.getWeapons().size() > 0) {
            do {
                System.out.println("Choose weapon to use: (0/1/2)\n" + hand.getWeapons().toString());
                choice = sc.nextInt();
            }while(choice > 2 || choice < 0);
            hand.playCard(choice,'w');
        } else {
            //implement a try catch
            System.out.println("No cards to use!");
        }
    }

    public void usePowerup() {
        Scanner sc = new Scanner(System.in);
        int choice;
        if(hand.getPowerups().size() > 0) {
            do {
                System.out.println("Choose powerup to use: \n" + hand.getPowerups().toString());
                choice = sc.nextInt();
            }while(choice > 2 || choice < 0);
            hand.playCard(choice,'w');
        } else {
            //implement a try catch
            System.out.println("No cards to use!");
        }
    }


    /**
     * @return
     */
    public void collect() {
        //TODO once we sorted out the item position
    }

    public void showHand(){
        System.out.println(hand.toString());
    }

    /**
     * @return
     */
    public PlayerState getPlayerState() {
        return null;
    }

    public void receiveBullet(Bullet b){
        board.addDamage(b.getDamage());
        board.addMarks(b.getMarks());
        //character.move();
    }


}


/*
        I added a method in WeaponCard called chooseCharacter, you have to call it before calling play(). You have to pass the square to the method
        so that he can tell you who you can choose.
        
        ArrayList<ArrayList<Character>> possibleTargets;
        possibleTargets = weapon.getPossibleTargets(); //This returns the list of characters I can shoot
        weapon.charge();


        String s;
        //The user will have to choose the character he wants to shoot. He could for example insert an int to indicate the index of the list.
        System.out.println("Enter the index of the character you want to shoot : ");

        try{
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            s = bufferRead.readLine();

            System.out.println("You chose to shoot character number :  " s);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
*/