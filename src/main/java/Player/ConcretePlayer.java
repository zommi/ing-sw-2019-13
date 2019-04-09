package Player;

import Cards.*;
import Map.Map;
import Map.*;
import Exceptions.*;
import GameBoard.*;

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
    public ConcretePlayer(String name, GameBoard gameBoard, Figure figure) {
        this.name = name;
        this.character = new Character(chooseFigure(figure));
        this.hand = new PlayerHand(this);
        this.board = new PlayerBoard(this);
        this.currentGameBoard = gameBoard;
    }

    public Character getCharacter(){
        return character;
    }


    public void move(char move) {
        SquareAbstract currentPos = character.getPosition();
        switch (move){
            case 'N':
                character.move(currentPos.getnSquare());
                break;
            case 'S':
                character.move(currentPos.getsSquare());
                break;
            case 'E':
                character.move(currentPos.geteSquare());
                break;
            case 'W':
                character.move(currentPos.getwSquare());
                break;
            default:
                break;
            }
    }

    public void shoot(int weaponIndex) {
        try {
            hand.playCard(weaponIndex,'w');
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

    public void usePowerup(int powerupIndex) {
        try{
            hand.playCard(powerupIndex,'w');
        }catch(InvalidMoveException e){
            e.printStackTrace();
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

    public void receiveBullet(Bullet b, int color){
        board.addDamage(b.getDamage(),color);
        try{
            board.addMarks(b.getMarks(),color);
        }catch (InvalidMoveException e){
            System.out.println("Max number of marks reached");
        }
        try {
            character.move(Map.getSquareFromXY(b.getX(), b.getY()));
        }catch (NoSuchSquareException e){
            e.printStackTrace();
        }
    }

    public void spawn(SquareAbstract sp){
        character.spawn(sp);
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