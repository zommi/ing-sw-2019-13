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


    public int getDamage() {
        return this.damage;
    }


    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public boolean getTeleporterFlag(){
        return this.teleporterflag;
    }

    public int getMarks() {
        return this.marks;
    }

    public ArrayList<AmmoCube> getCost(){
        return (ArrayList<AmmoCube>) this.cost.clone();
    }


}