package Cards;

import java.util.*;
import Items.*;


public class Weapon {

    private Bullet bullet;
    private Command command;
    private int x;  //I need it if I have to use the teleporter.
    private int y;  //I need it if I have to use the teleporter.
    private int damage;
    private int marks;
    private boolean teleporterflag; //If this is true I can use the move() method in character with the new coordinates.
    private ArrayList<AmmoCube> cost;

    public Weapon() {
        //TODO reads from file the constants of the bullet and initializes it
    }

    public Bullet shoot() { //fills the bullet
        bullet = new Bullet(this.x, this.y, this.damage, this.marks, this.teleporterflag,this.cost);
        return(bullet);
    }

    public Command getCommand(){
        return this.command;
    }


    public Bullet charge() {
        // TODO
        return null;
    }

}