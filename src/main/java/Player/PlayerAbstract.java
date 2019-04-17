package Player;

import java.util.*;

import Cards.Bullet;
import Constants.Color;
import Constants.*;
import Map.*;

/**
 *
 */
public abstract class PlayerAbstract {

    /**
     * @return
     */
    PlayerAbstract(){
    }


    /**
     *Binds a character to the figure the player wants
     * and sets its status to Taken
     * @param selection figure the player selected
     * @return the figure the player selected
     */
    public Figure chooseFigure(Figure selection) {
        System.out.println("You chose: " + selection +". Great Choice!");
        Character.setTaken(selection);
        return selection;
    }

    /**
     * Once the player has 11 or more tokens on his PlayerBoard he loses a life
     * and his character needs to be respawned.
     * @param sp SpawnPoint in which the player wants to place its character
     */
    public abstract void spawn(SquareAbstract sp);

    /**
     *Moves the character linked to the player by one square at a time
     * @param direction direction to follow
     */
    public void move(Directions direction) {
    }

    public void shoot() {
    }

    public void collect() {
    }

    public abstract Character getCharacter();

    public PlayerState getPlayerState() {
        return null;
    }

    public abstract void receiveBullet(Bullet b, Color color);

    public abstract void doAction();

    public abstract void setAction(Action action);

    public abstract

}