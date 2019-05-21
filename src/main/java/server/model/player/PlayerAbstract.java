package server.model.player;

import constants.Color;
import constants.Direction;
import client.Cost;
import server.model.items.AmmoCube;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 */
public abstract class PlayerAbstract implements Serializable {

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
        GameCharacter.setTaken(selection);
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
    public abstract void move(Direction direction);

    public void shoot() {
    }

    public abstract void setPlayerCharacter(Figure figure);

    public abstract void collect(Square square);

    public abstract String getCharacterName();

    public abstract void collect(SpawnPoint spawnPoint, int choice);

    public abstract GameCharacter getGameCharacter();

    public abstract SquareAbstract getPosition();

    public PlayerState getPlayerState() {
        return null;
    }

    public abstract void usePowerup(int powerupIndex);

    public abstract UUID getId();

    public abstract PlayerBoard getBoard();

    public abstract Color getColor();

    public abstract PlayerState currentState();

    public abstract void drawPowerup();

    public abstract void setState(PlayerState state);

    public abstract boolean canPay(ArrayList<AmmoCube> cost);

    public abstract boolean canPay(Cost cost);

    public abstract String getName();

    public abstract void setOldPosition();

    public abstract SquareAbstract getOldPosition();

    public abstract void addDamage(int damage, Color color);

    public abstract void addMarks(int marks, Color color);
}