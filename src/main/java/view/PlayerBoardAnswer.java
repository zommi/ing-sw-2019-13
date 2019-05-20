package view;

import constants.Color;
import java.util.List;

public class PlayerBoardAnswer implements ServerAnswer{
    private Color[] damage;
    private int damageTaken;

    private List<Color> marks;
    private int[] pointValue;
    private int currentPointValueCursor;
    private int numberOfDeaths;

    //0 red, 1 blue, 2 yellow
    private int[] ammo = {1,1,1};
    private int points;

    public PlayerBoardAnswer(Color[] damage, int damageTaken, List<Color> marks, int[] pointValue, int currentPointValueCursor, int numberOfDeaths, int[] ammo, int points) {
        this.damage = damage;
        this.damageTaken = damageTaken;
        this.marks = marks;
        this.pointValue = pointValue;
        this.currentPointValueCursor = currentPointValueCursor;
        this.numberOfDeaths = numberOfDeaths;
        this.ammo = ammo;
        this.points = points;
    }

    public Color[] getDamage(){
        return this.damage;
    }
    public int getDamageTaken(){
        return this.damageTaken;
    }
    public List<Color> getMarks(){
        return this.marks;
    }
    public int[] getPointValue(){
        return this.pointValue;
    }
    public int getCurrentPointValueCursor(){
        return this.currentPointValueCursor;
    }
    public int getNumberOfDeaths(){
        return this.numberOfDeaths;
    }
    public int[] getAmmo(){
        return this.ammo;
    }
    public int getPoints(){
        return this.points;
    }
}
