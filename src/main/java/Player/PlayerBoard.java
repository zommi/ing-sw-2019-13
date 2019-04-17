package Player;

import Cards.AmmoTile;
import Map.*;
import Constants.*;
import Exceptions.*;
import Items.*;

import java.util.*;

/**
 * 
 */
public class PlayerBoard{
    ConcretePlayer player;

    private Color[] damage;
    private int damageTaken;

    private List<Color> marks;
    private int[] pointValue;
    private int currentPointValueCursor;
    private int numberOfDeaths;

    private ArrayList<AmmoCube> redAmmo;
    private int validRedAmmo;

    private ArrayList<AmmoCube> yellowAmmo;
    private int validYellowAmmo;

    private ArrayList<AmmoCube> blueAmmo;
    private int validBlueAmmo;

    private int points;

    /**
     * Default constructor
     */
    public PlayerBoard(ConcretePlayer p) {
        this.player = p;
        this.damage = new Color[Constants.MAX_HP];
        this.damageTaken = 0;
        this.marks = new ArrayList<Color>();
        this.numberOfDeaths = 0;
        this.pointValue = Constants.POINT_VALUE;
        this.currentPointValueCursor = 0;
        this.redAmmo = new ArrayList<AmmoCube>();
        this.validRedAmmo = 0;
        this.yellowAmmo = new ArrayList<AmmoCube>();
        this.validYellowAmmo = 0;
        this.blueAmmo = new ArrayList<AmmoCube>();
        this.validBlueAmmo = 0;
    }

    private void setDamage(int i, Color color) {
        this.damage[i] = color;
    }

    /**
     * @return
     */
    public int getDamageTaken() {
        return damageTaken;
    }


    public Color getTokenColor(int index){
        return damage[index];
    }

    public List<Color> getMarks(){
        return this.marks;
    }

    /**
     * @return
     */
    public int getNumberOfDeaths() {
        return numberOfDeaths;
    }

    /**
     * @return
     */
    public List<AmmoCube> getValidRedAmmoCube() {
        return (ArrayList<AmmoCube>) redAmmo.clone();
    }

    public List<AmmoCube> getValidYellowAmmoCube() {
        return (ArrayList<AmmoCube>) yellowAmmo.clone();
    }

    public List<AmmoCube> getValidBlueAmmoCube() {
        return (ArrayList<AmmoCube>) blueAmmo.clone();
    }

    /**
     * @return
     */
    //TODO add easter egg when someone dies 7 times in the same game and make him win.
    public void addSkull() {
        if(damageTaken > 10){
            numberOfDeaths++;
            currentPointValueCursor++;
        } else{
            System.err.println("PLAYER STILL ALIVE");
        }
    }

    public int getPointValue(){
        return pointValue[currentPointValueCursor];
    }

    public void addDamage(int damageInBullet, Color color) {
        int marksActivated = getMarksOfAColor(color);
        removeMarksOfAColor(color);
        for(int i = damageTaken; i < damageTaken+marksActivated;i++){
            setDamage(i,color);
        }
        damageTaken += damageInBullet + marksActivated;
        if(damageTaken > 10){
            //add skull on player board, remove skull from game board, decrease value of player,
            //if damage == 12 place a mark on killer
            addSkull();
            damageTaken = 0;
            for(int i = 0; i < Constants.MAX_HP; i++){
                this.damage[i] = Color.UNDEFINED;
            }
        }
    }

    private void removeMarksOfAColor(Color color) {
        //get rid of all token of a color
        marks.removeAll(Collections.singleton(color));
    }

    private int getMarksOfAColor(Color color) {
        int counter = 0;
        for(Color c : marks){
            if(c == color){counter++;}
        }
        return counter;
    }

    //add mark on playerboard, if size > 3 then throw exception
    public void addMarks(int marks, Color color)
            throws InvalidMoveException {
        for(int i = 0; i < marks; i++) {
            if (this.marks.size() <= 3) {
                this.marks.add(color);
            } else {
                throw new InvalidMoveException();
            }
        }
    }

    public void addAmmo(AmmoTile ammo) {
    }
}