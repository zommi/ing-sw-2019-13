package server.model.player;

import java.util.*;

import server.model.Controller.PlayerAction.Action;
import server.model.cards.Bullet;
import constants.Color;
import constants.*;
import server.model.map.*;

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

    public abstract SquareAbstract getPosition();

    public PlayerState getPlayerState() {
        return null;
    }

    public abstract void receiveBullet(Bullet b, Color color);

    public abstract void doAction();

    public abstract void setAction(Action action);

    public abstract void usePowerup(int powerupIndex);

    public abstract UUID getId();

    public abstract PlayerBoard getBoard();

}