package server.model.player;

import server.model.cards.AmmoTile;
import constants.*;
import exceptions.*;
import server.model.cards.WeaponCard;
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
    private int row;
    private int col;
    private String characterName;
    private List<WeaponCard> unloadedWeapons;


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
        unloadedWeapons = new ArrayList<>();
    }

    public void removeWeaponCard(WeaponCard weaponCard){
        Iterator<WeaponCard> iterator = unloadedWeapons.iterator();
        WeaponCard weaponCard1;
        while(iterator.hasNext()){
            weaponCard1 = iterator.next();
            if(weaponCard.equals(weaponCard1)){
                iterator.remove();
                return;
            }

        }
    }

    public void addUnloadedWeapon(WeaponCard weaponCard){
        unloadedWeapons.add(weaponCard);
    }

    public List<WeaponCard> getUnloadedWeapons(){
        return unloadedWeapons;
    }

    public void removeUnloadedWeapon(WeaponCard weaponCard){
        Iterator<WeaponCard> iterator = unloadedWeapons.iterator();
        while(iterator.hasNext()){
            WeaponCard weaponCard1 = iterator.next();
            if(weaponCard1.equals(weaponCard))
                iterator.remove();
        }
    }

    private void setDamage(int i, Color color) {
        this.damage[i] = color;
    }

    public void setCharacterName(String string){
        this.characterName = string;
    }

    public String getCharacterName(){
        return this.characterName;
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

    public int getAmmo(Color color){
        return ammo[color.getIndex()];
    }

    public void spawn(int row, int col){
        this.row = row;
        this.col = col;
    }

    public void setPosition(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
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
        if((damageTaken > Constants.BETTERCOLLECTDAMAGE)&&((damageTaken <= Constants.BETTERSHOOTDAMAGE))){
            player.setState(PlayerState.BETTER_COLLECT);
        }
        if((damageTaken > Constants.BETTERSHOOTDAMAGE) && (damageTaken <= Constants.DEATH_THRESHOLD)){
            player.setState(PlayerState.BETTER_SHOOT);
        }
        if(damageTaken > Constants.DEATH_THRESHOLD){
            //add skull on player board, remove skull from game board, decrease value of player,
            //if damage == 12 place a mark on killer
            addSkull(color);
            damageTaken = 0;
            for(int i = 0; i < Constants.MAX_HP; i++){
                this.damage[i] = Color.UNDEFINED;
            }
            player.setState(PlayerState.TOBESPAWNED);
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

    public void decreaseAmmo(Color color){
        int index = 0;
        switch(color){
            case RED: index = RED_CUBES_INDEX; break;
            case BLUE: index = BLUE_CUBES_INDEX; break;
            case YELLOW: index = YELLOW_CUBES_INDEX; break;
            default: //this should never happen
        }
        ammo[index]--;
    }
}