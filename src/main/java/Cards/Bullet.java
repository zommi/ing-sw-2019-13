package Cards;

import java.util.*;
import Items.*;

/**
 * 
 */
public class Bullet {


    private int x;  //I need it if I have to use the teleporter.
    private int y;  //I need it if I have to use the teleporter.
    private int damage;
    private int marks;
    private boolean teleporterflag; //If this is true I can use the move() method in character with the new coordinates.
    private ArrayList<AmmoCube> cost;

    public Bullet(int x, int y, int damage, int marks, boolean teleporterflag, ArrayList<AmmoCube> cost) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.marks = marks;
        this.teleporterflag = teleporterflag;
        this.cost = cost;
    }


    /**
     * Returns the value of damage
     * @return the value of damage
     */
    public int getDamage() {
        return this.damage;
    }


    /**
     * Returns the value of X
     * @return the value of X
     */
    public int getX(){
        return this.x;
    }

    /**
     * Returns the value of Y
     * @return the value of Y
     */
    public int getY(){
        return this.y;
    }

    /**
     * Returns the value of teleporterflag
     * @return the value of teleporterflag
     */
    public boolean getTeleporterFlag(){
        return this.teleporterflag;
    }

    /**
     * Returns the value of marks
     * @return the value of marks
     */
    public int getMarks() {
        return this.marks;
    }

    /**
     * Returns the ArrayList of Ammocubes representing the cost
     * @return the ArrayList of Ammocubes representing the cost
     */
    public ArrayList<AmmoCube> getCost(){
        return (ArrayList<AmmoCube>) this.cost.clone();
    }


}