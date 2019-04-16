package Player;

import Cards.*;
import Map.Map;
import Map.*;
import Exceptions.*;
import GameBoard.*;
import Constants.*;

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
    private UUID id;

    /**
     *Creates a player by giving him a name, a unique id, a hand
     * containing its cards and a playerboard containing his info.
     * @param name name chosen by a player
     * @param gameBoard each player moves in the context of the same gameBoard
     * @param figure each player moves on the gameboard thanks to its character and figure
     */
    public ConcretePlayer(String name, GameBoard gameBoard, Figure figure) {
        this.name = name;
        this.character = new Character(chooseFigure(figure));
        this.hand = new PlayerHand(this);
        this.board = new PlayerBoard(this);
        this.currentGameBoard = gameBoard;
        id = UUID.randomUUID();
    }

    public Character getCharacter(){
        return character;
    }


    /**
     * Moves the character to an adjacent square in a direction
     * @param move char that belongs to ('n','s','w','e')
     */
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

    /**
     * Uses a weapon in the player's hand
     * @param weaponIndex int between 0 and 2 corresponding to a weaponCard
     */
    public void shoot(int weaponIndex) {
        try {
            hand.playCard(weaponIndex,'w');
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uses a powerup in the player's hand
     * @param powerupIndex int between 0 and 2 corresponding to a powerupCard
     */
    public void usePowerup(int powerupIndex) {
        try{
            hand.playCard(powerupIndex,'w');
        }catch(InvalidMoveException e){
            e.printStackTrace();
        }
    }


    /**
     * Action that collects an ammo tile from a square.
     * @param item ammotile on the square
     */
    public void collect(AmmoTile item) {
        board.addAmmo(item);
    }

    /**
     * action that collects a weapon on a spawpoint
     * @param card card on spawnpoint
     */
    public void collect(WeaponCard card){
        hand.addCard(card);
    }

    /**
     * Shows to a player his own hand
     */
    public void showHand(){
        System.out.println(hand.toString());
    }

    /**
     * @return
     */
    public PlayerState getPlayerState() {
        return null;
    }


    /**
     * If a player get shot he is the one who needs to process the damage.
     * @param b the bullet that contains the informations that need to be changed
     * @param color the color of the tokens or marks that need to be added
     */
    public void receiveBullet(Bullet b, Color color){
        board.addDamage(b.getDamage(),color);
        try{
            board.addMarks(b.getMarks(),color);
        }catch (InvalidMoveException e){
            System.out.println("Max number of marks reached");
        }
        try {
            character.move(Map.getSquare(b.getX(), b.getY()));
        }catch (NoSuchSquareException e){
            e.printStackTrace();
        }
    }

    /**
     * Places a character in a Spawnpoint
     * @param sp SpawnPoint in which the player wants to place its character
     */
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