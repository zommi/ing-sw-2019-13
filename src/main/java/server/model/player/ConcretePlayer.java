package server.model.player;

import server.model.cards.*;
import server.model.items.AmmoCube;
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
    private GameCharacter gameCharacter;
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
     * @param figure each player moves on the gameboard thanks to its gameCharacter and figure
     */
    public ConcretePlayer(String name, GameBoard gameBoard, Figure figure) {
        this.name = name;
        this.gameCharacter = new GameCharacter(chooseFigure(figure));
        this.hand = new PlayerHand(this);
        this.board = new PlayerBoard(this);
        this.currentGameBoard = gameBoard;
        this.state = PlayerState.NORMAL;
        id = UUID.randomUUID();
    }

    public ConcretePlayer(String name) {
        this.name = name;
        id = UUID.randomUUID();
    }

    public GameCharacter getGameCharacter(){
        return gameCharacter;
    }

    public PlayerBoard getBoard(){return board;}

    public GameBoard getCurrentGameBoard(){return this.currentGameBoard;}


    /**
     * Moves the gameCharacter to an adjacent square in a direction
     * @param direction direction to follow
     */
    public void move(Directions direction) {
        SquareAbstract currentPos = gameCharacter.getPosition();
        switch (direction){
            case NORTH:
                gameCharacter.move(currentPos.getnSquare());
                break;
            case SOUTH:
                gameCharacter.move(currentPos.getsSquare());
                break;
            case EAST:
                gameCharacter.move(currentPos.geteSquare());
                break;
            case WEST:
                gameCharacter.move(currentPos.getwSquare());
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


    public void collect(Square square){
        this.board.processAmmoTile(square.getAmmoTile());
    }

    public void collect(SpawnPoint spawnPoint, int choice){
        if(!this.hand.weaponFull()) this.hand.addCard(spawnPoint.getWeaponCards().get(choice));
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

    public SquareAbstract getPosition(){ return this.gameCharacter.getPosition();}

    /**
     * Places a gameCharacter in a Spawnpoint
     * @param sp SpawnPoint in which the player wants to place its gameCharacter
     */
    public void spawn(SquareAbstract sp){
        gameCharacter.spawn(sp);
    }


    public UUID getId(){
        return this.id;
    }

    public Color getColor(){
        return this.gameCharacter.getColor();
    }

    @Override
    public PlayerState currentState() {
        return state;
    }

    @Override
    public void drawPowerup() {
        if(!this.hand.powerupsFull()) {
            this.hand.addCard(currentGameBoard.getPowerupDeck().draw());
        }
    }


    public void setState(PlayerState state){
        this.state = state;
    }

    @Override
    public boolean canPay(ArrayList<AmmoCube> cost) {
        return this.board.canPay(cost);
    }

    @Override
    public boolean canPay(Cost cost) {      //TODO
        return true;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setOldPosition() {
        this.gameCharacter.setOldPosition();
    }

    @Override
    public SquareAbstract getOldPosition() {
        return this.gameCharacter.getOldPosition();
    }

    @Override
    public void addDamage(int damage, Color color) {
        this.board.addDamage(damage, color);
    }

    @Override
    public void addMarks(int marks, Color color) {
        this.board.addMarks(marks, color);
    }

}


/*
        I added a method in WeaponCard called chooseCharacter, you have to call it before calling play().
        You have to pass the square to the method so that he can tell you who you can choose.

        ArrayList<ArrayList<GameCharacter>> possibleTargets;
        possibleTargets = weapon.getPossibleTargets(); //This returns the list of characters I can shoot
        weapon.charge();

*/