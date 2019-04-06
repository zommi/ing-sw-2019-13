
import java.util.*;

/**
 * 
 */
public class Bullet {

    public Bullet() {
    }


    private int damage;
    private int push;
    private char pushOrientation;
    private int marks;
    private boolean teleporterflag; //If this is true I can use the move() method in character with the new coordinates.


    public int getDamage() {
        return this.damage;
    }


    public boolean getTeleporterFlag(){
        return this.teleporterflag;
    }

    public int getMarks() {
        return this.marks;
    }


    public char getPushOrientation() {
        return this.pushOrientation;
    }


    public int getPushValue() {
        return this.push;
    }

}