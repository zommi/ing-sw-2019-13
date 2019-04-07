
import java.util.*;

/**
 * 
 */
public class Bullet {


    private int x;  //I need it if I have to use the teleporter.
    private int y;  //I need it if I have to use the teleporter.
    private int damage;
    private int marks;
    private boolean teleporterflag; //If this is true I can use the move() method in character with the new coordinates.

    public Bullet(int x, int y, int damage, int marks, int teleporterflag) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.marks = marks;
        this.teleporterflag = teleporterflag;
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


}