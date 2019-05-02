package server.model.player;

import server.model.cards.*;
import server.model.map.*;
import exceptions.*;
import server.model.gameboard.*;
import constants.*;

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
    private PlayerState state;

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

    public PlayerBoard getBoard(){return board;}

    public GameBoard getCurrentGameBoard(){return this.currentGameBoard;}


    /**
     * Moves the character to an adjacent square in a direction
     * @param direction direction to follow
     */
    public void move(Directions direction) {
        SquareAbstract currentPos = character.getPosition();
        switch (direction){
            case NORTH:
                character.move(currentPos.getnSquare());
                break;
            case SOUTH:
                character.move(currentPos.getsSquare());
                break;
            case EAST:
                character.move(currentPos.geteSquare());
                break;
            case WEST:
                character.move(currentPos.getwSquare());
                break;
            default:
                break;
            }
    }


    /**
     * Uses a powerup in the player's hand
     * @param powerupIndex int between 0 and 2 corresponding to a powerupCard
     */
    public void usePowerup(int powerupIndex) {
        hand.playPowerup(powerupIndex);
    }



    /**
     * Shows a player his own hand
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

    public SquareAbstract getPosition(){ return this.character.getPosition();}


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
            character.move(GameMap.getSquare(b.getY(), b.getX()));
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


    public UUID getId(){
        return this.id;
    }

    public Color getColor(){
        return this.character.getColor();
    }
}


/*
        I added a method in WeaponCard called chooseCharacter, you have to call it before calling play().
        You have to pass the square to the method so that he can tell you who you can choose.

        ArrayList<ArrayList<Character>> possibleTargets;
        possibleTargets = weapon.getPossibleTargets(); //This returns the list of characters I can shoot
        weapon.charge();

*/