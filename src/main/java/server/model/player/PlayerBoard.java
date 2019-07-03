package server.model.player;

import server.model.cards.AmmoTile;
import constants.*;
import exceptions.*;
import server.model.cards.WeaponCard;
import server.model.game.GameState;
import server.model.items.*;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class PlayerBoard implements Serializable {

    private ConcretePlayer player;

    private List<Color> damage;

    private List<Color> marks;
    private int[] pointValue;
    private int currentPointValueCursor;
    private int row;
    private int col;
    private String characterName;
    private List<WeaponCard> unloadedWeapons;
    private Color killerColor;
    private boolean turned;

    private int weaponHandSize;
    private int powerUpHandSize;

    //0 red, 1 blue, 2 yellow
    private int[] ammo = {Constants.NUM_AMMO_START,Constants.NUM_AMMO_START,Constants.NUM_AMMO_START};

    private int points;

    private static final int RED_CUBES_INDEX = Color.RED.getIndex();
    private static final int BLUE_CUBES_INDEX = Color.BLUE.getIndex();
    private static final int YELLOW_CUBES_INDEX = Color.YELLOW.getIndex();




    public PlayerBoard(ConcretePlayer p) {
        this.player = p;
        this.damage = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.pointValue = Constants.POINTS_VALUES;
        this.currentPointValueCursor = 0;
        unloadedWeapons = new ArrayList<>();
    }

    public Color getFirstDamageColor(){
        if(!damage.isEmpty())
            return damage.get(0);
        else
            return null;
    }

    public void increaseWeaponHandSize(){
        weaponHandSize++;
    }

    public void decreaseWeaponHandSize(){
        weaponHandSize--;
    }

    public int getWeaponHandSize() {
        return weaponHandSize;
    }

    public void increasePowerUpHandSize(){
        powerUpHandSize++;
    }

    public void decreasePowerUpHandSize(){
        powerUpHandSize--;
    }

    public int getPowerUpHandSize() {
        return powerUpHandSize;
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
        if((damage.size() > Constants.ADR_COLLECT_THRESHOLD && damage.size() <= Constants.ADR_SHOOT_THRESHOLD &&
                player.getCurrentGame().getCurrentState() != GameState.FINAL_FRENZY)){
            player.setState(PlayerState.ADRENALINIC_COLLECT);
        }
        else if(damage.size() > Constants.ADR_SHOOT_THRESHOLD && damage.size() <= Constants.DEATH_THRESHOLD &&
                player.getCurrentGame().getCurrentState() != GameState.FINAL_FRENZY){
            player.setState(PlayerState.ADRENALINIC_SHOOT);
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
        player.setState(PlayerState.DEAD);
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

    public int getDamageOfAColor(Color color){
        int counter = 0;
        for(Color c : damage){
            if (c == color) {
                counter++;
            }
        }
        return counter;
    }

    public List<Color> getAttackersInOrder(){
        List<Color> result = new ArrayList<>();
        for(Color c: damage){
            if(!result.contains(c))result.add(c);
        }
        return result;
    }

    //add mark on playerboard, if size > 3 then don't add
    public void addMarks(int marks, Color color) {
        for(int i = 0; i < marks; i++) {
            if (getMarksOfAColor(color) < Constants.MAX_MARKS) {
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

    public void increaseAmmo(Color color){
        int index = 0;
        switch(color){
            case RED: index = RED_CUBES_INDEX; break;
            case BLUE: index = BLUE_CUBES_INDEX; break;
            case YELLOW: index = YELLOW_CUBES_INDEX; break;
            default:
        }
        ammo[index]++;
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

    public void turnPlayerBoard(){
        this.pointValue = Constants.FINAL_FRENZY_POINT_VALUE;
        this.currentPointValueCursor = 0;
        turned = true;
    }

    public void setAmmo(int[] ammo) {
        this.ammo[0] = ammo[0];
        this.ammo[1] = ammo[1];
        this.ammo[2] = ammo[2];
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

    public boolean isTurned() {
        return turned;
    }
}