package server.model.player;

import client.weapons.Weapon;
import constants.Color;
import constants.Direction;
import client.weapons.Cost;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.game.Game;
import server.model.items.AmmoCube;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        //System.out.println("You chose: " + selection +". Great Choice!");
        GameCharacter.setTaken(selection);
        return selection;
    }

    /**
     * Once the player has 11 or more tokens on his PlayerBoard he loses a life
     * and his character needs to be respawned.
     * @param sp SpawnPoint in which the player wants to place its character
     */
    public abstract void spawn(SquareAbstract sp);

    public void shoot() {
    }

    public abstract int getPoints();

    public abstract void setCharacterChosen(boolean choice);

    public abstract boolean isCharacterChosen();

    public abstract int getClientID();

    public abstract void setCurrentGame(Game game);

    public abstract void setPlayerCharacter(Figure figure);

    public abstract PlayerHand getHand();

    public abstract WeaponCard getWeaponCard(Weapon weapon);

    public abstract void collect(Square square);

    public abstract PowerUpCard getPowerUpCard(PowerUpCard powerUpCard);

    public abstract  WeaponCard getWeaponCard(WeaponCard weaponCard);

    public abstract String getCharacterName();

    public abstract void collect(SpawnPoint spawnPoint, int choice);

    public abstract GameCharacter getGameCharacter();

    public abstract SquareAbstract getPosition();

    public PlayerState getPlayerState() {
        return null;
    }

    public abstract void usePowerup(int powerupIndex);

    public abstract PlayerBoard getPlayerBoard();

    public abstract Color getColor();

    public abstract PlayerState currentState();

    public abstract void drawPowerup();

    public abstract void setState(PlayerState state);

    public abstract boolean canPay(Cost cost);

    public abstract String getName();

    public abstract void addDamage(int damage, Color color);

    public abstract void addMarks(int marks, Color color);

    public abstract void setPosition(SquareAbstract square);

    public  abstract PlayerAbstract getJustDamagedBy();

    public abstract void setJustDamagedBy(PlayerAbstract playerAbstract);

    public abstract void pay(Cost cost);

    public abstract void payWithPowerUps(Cost cost, List<PowerUpCard> powerUpCards);

    public abstract boolean hasPowerUpCards(List<PowerUpCard> powerUpCards);

    public abstract boolean hasWeaponCards(List<WeaponCard> weaponCards);

    public abstract void die();

    public abstract boolean isOverkilled();

    public abstract Color getKillerColor();

    public abstract boolean isConnected();

    public abstract void setConnected(boolean connected);

    public abstract void addPoints(int i);

}