package server.model.player;

import server.model.cards.AmmoTile;
import constants.*;
import exceptions.*;
import server.model.items.*;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class PlayerBoard implements Serializable {
    ConcretePlayer player;

    private Color[] damage;
    private int damageTaken;

    private List<Color> marks;
    private int[] pointValue;
    private int currentPointValueCursor;
    private int numberOfDeaths;


    //0 red, 1 blue, 2 yellow
    private int[] ammo = {1,1,1};

    private int points;

    private static final int RED_CUBES_INDEX = Color.RED.getIndex();
    private static final int BLUE_CUBES_INDEX = Color.BLUE.getIndex();
    private static final int YELLOW_CUBES_INDEX = Color.YELLOW.getIndex();

    /**
     * Default constructor
     */

    public PlayerBoard(){

    }

    public PlayerBoard(ConcretePlayer p) {
        this.player = p;
        this.damage = new Color[Constants.MAX_HP];
        this.damageTaken = 0;
        this.marks = new ArrayList<>();
        this.numberOfDeaths = 0;
        this.pointValue = Constants.POINT_VALUE;
        this.currentPointValueCursor = 0;
    }

    private void setDamage(int i, Color color) {
        this.damage[i] = color;
    }

    public PlayerBoard createCopy(PlayerBoard playerBoardToCopy){
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.player = null;
        playerBoard.damageTaken = playerBoardToCopy.getDamageTaken();
        playerBoard.damage = playerBoardToCopy.getDamage();
        playerBoard.marks = playerBoardToCopy.getMarks();
        playerBoard.numberOfDeaths = playerBoardToCopy.getNumberOfDeaths();
        playerBoard.pointValue = playerBoardToCopy.getPointValueArray();
        playerBoard.currentPointValueCursor = playerBoardToCopy.getCurrentPointValueCursor();
        playerBoard.ammo = new int[3];
        playerBoard.ammo[0] = ammo[RED_CUBES_INDEX];
        playerBoard.ammo[1] = ammo[BLUE_CUBES_INDEX];
        playerBoard.ammo[2] = ammo[YELLOW_CUBES_INDEX];
        return playerBoard;
    }

    public ConcretePlayer getPlayer() {
        return player;
    }

    public Color[] getDamage() {
        return damage;
    }

    public int getCurrentPointValueCursor(){
        return this.currentPointValueCursor;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public Color getTokenColor(int index){
        return damage[index];
    }

    public List<Color> getMarks(){
        return this.marks;
    }

    public int getNumberOfDeaths() {
        return numberOfDeaths;
    }

    public int getRedAmmo() {
        return ammo[RED_CUBES_INDEX];
    }

    public int getBlueAmmo() {
        return ammo[BLUE_CUBES_INDEX];
    }

    public int getYellowAmmo() {
        return ammo[YELLOW_CUBES_INDEX];
    }

    //TODO add easter egg when someone dies 7 times in the same game and make him win.
    public void addSkull(Color colorOfAttacker) {
        if(damageTaken > Constants.DEATH_THRESHOLD){
            int token2add = damageTaken >= Constants.MAX_HP ? 2 : 1;
            numberOfDeaths++;
            currentPointValueCursor++;
            player.getCurrentGame().getCurrentGameBoard().getTrack().removeSkull(token2add,colorOfAttacker);
        } else{
            System.err.println("PLAYER STILL ALIVE");
        }
    }

    public int[] getPointValueArray(){
        return this.pointValue;
    }

    public int getPointValue(){
        return pointValue[currentPointValueCursor];
    }

    public void addDamage(int damageInBullet, Color color) {
        int marksActivated = getMarksOfAColor(color);
        removeMarksOfAColor(color);
        for(int i = damageTaken; i < damageTaken+marksActivated+damageInBullet;i++){
            setDamage(i,color);
        }
        damageTaken += damageInBullet + marksActivated;
        if(damageTaken > Constants.DEATH_THRESHOLD){
            //add skull on player board, remove skull from game board, decrease value of player,
            //if damage == 12 place a mark on killer
            addSkull(color);
            damageTaken = 0;
            for(int i = 0; i < Constants.MAX_HP; i++){
                this.damage[i] = Color.UNDEFINED;
            }
        }
    }

    public void removeMarksOfAColor(Color color) {
        //get rid of all token of a color
        marks.removeAll(Collections.singleton(color));
    }

    public int getMarksOfAColor(Color color) {
        int counter = 0;
        for(Color c : marks){
            if(c == color){counter++;}
        }
        return counter;
    }

    //add mark on playerboard, if size > 3 then don't add
    public void addMarks(int marks, Color color)
             {
        for(int i = 0; i < marks; i++) {
            if (this.marks.size() < Constants.MAX_MARKS) {
                this.marks.add(color);
            }
        }
    }


    public void useAmmo(List<AmmoCube> cost) throws InvalidMoveException{
        for(AmmoCube cube : cost){
            if(this.ammo[cube.getColor().getIndex()] > 0)this.ammo[cube.getColor().getIndex()]--;
            else throw new InvalidMoveException();
        }
    }

    public void processAmmoTile(AmmoTile ammoTile) {
        for(AmmoCube cube : ammoTile.getContent()){
            if(this.ammo[cube.getColor().getIndex()] < 3)this.ammo[cube.getColor().getIndex()]++;
        }
        if(ammoTile.hasPowerup()){
            this.player.drawPowerup();
        }
    }

    public boolean canPay(List<AmmoCube> cost) {
        int[] counter = new int[3];

        for(AmmoCube cube : cost){
            counter[cube.getColor().getIndex()]++;
            if(counter[cube.getColor().getIndex()] > ammo[cube.getColor().getIndex()]){
                return false;
            }
        }
        return true;
    }
}