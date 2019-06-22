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

    private List<Color> damage;

    private List<Color> marks;
    private int[] pointValue;
    private int currentPointValueCursor;
    private int row;
    private int col;
    private String characterName;
    private List<WeaponCard> unloadedWeapons;
    private Color killerColor;


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
        this.damage = new ArrayList<>();
        this.marks = new ArrayList<>();
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

    public void addDamage(int damageToAdd, Color color) {
        for(int i = 0; i < damageToAdd && i < Constants.MAX_HP;i++){
            this.damage.add(color);
        }
        if((damage.size() > Constants.BETTERCOLLECTDAMAGE)&&(damage.size() <= Constants.BETTERSHOOTDAMAGE)){
            player.setState(PlayerState.BETTER_COLLECT);
        }
        if((damage.size() > Constants.BETTERSHOOTDAMAGE) && (damage.size() <= Constants.DEATH_THRESHOLD)){
            player.setState(PlayerState.BETTER_SHOOT);
        }
        if(damage.size() > Constants.DEATH_THRESHOLD){
            this.killerColor = color;
        }
    }

    public void marks2Damage(Color color){
        int marksActivated = getMarksOfAColor(color);
        removeMarksOfAColor(color);
        for(int i = 0; i < marksActivated && i < Constants.MAX_HP;i++){
            damage.add(color);
        }
    }

    public void processDeath() {
        damage.clear();
        currentPointValueCursor++;
        player.setState(PlayerState.TOBESPAWNED);
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
            if(this.ammo[cube.getColor().getIndex()] < 3)
                this.ammo[cube.getColor().getIndex()]++;
        }
        if(ammoTile.hasPowerup()){
            player.drawPowerup();
        }
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

    public void setCharacterName(String string){
        this.characterName = string;
    }

    public String getCharacterName(){
        return this.characterName;
    }

    public ConcretePlayer getPlayer() {
        return player;
    }

    public List<Color> getDamage() {
        return damage;
    }

    public int getCurrentPointValueCursor(){
        return this.currentPointValueCursor;
    }

    public int getDamageTaken() {
        return damage.size();
    }

    public Color getTokenColor(int index){
        return damage.get(index);
    }

    public List<Color> getMarks(){
        return this.marks;
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

    public int[] getPointValueArray(){
        return this.pointValue;
    }

    public int getPointValue(){
        return pointValue[currentPointValueCursor];
    }

    public Color getKillerColor() {
        return killerColor;
    }
}