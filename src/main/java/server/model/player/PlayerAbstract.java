package server.model.player;

import client.weapons.Cost;
import client.weapons.Weapon;
import constants.Color;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.game.Game;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;

import java.io.Serializable;
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

    public abstract Game getCurrentGame();

    public abstract int getPoints();

    public abstract void setCharacterChosen(boolean choice);

    public abstract boolean isCharacterChosen();

    public abstract int getClientID();

    public abstract void setCurrentGame(Game game);

    public abstract void setPlayerCharacter(Figure figure);

    public abstract PlayerHand getHand();

    public abstract WeaponCard getWeaponCard(Weapon weapon);

    /**
     * collects ammotile from a square
     * @param square destination square to collect from
     */
    public abstract void collect(Square square);

    public abstract PowerUpCard getPowerUpCard(PowerUpCard powerUpCard);

    public abstract  WeaponCard getWeaponCard(WeaponCard weaponCard);

    public abstract String getCharacterName();

    /**
     * Collects a weapon from a spawn point.
     * @param spawnPoint Destination spawnpoint of ammo.
     * @param choice Index of weapon to collect
     */
    public abstract void collect(SpawnPoint spawnPoint, int choice);

    public abstract GameCharacter getGameCharacter();

    public abstract SquareAbstract getPosition();

    public PlayerState getPlayerState() {
        return null;
    }

    public abstract PlayerBoard getPlayerBoard();

    public abstract Color getColor();

    public abstract PlayerState currentState();

    public abstract void drawPowerup();

    public abstract void drawPowerupNoLimits();

    public abstract void setState(PlayerState state);

    /**
     * Method that checks if a player can pay a given cost.
     * @param cost Cost in ammocubes to pay
     * @return The method returns true if the player can pay the cost.
     */
    public abstract boolean canPay(Cost cost);

    public abstract String getName();

    /**
     * Add damage to player's playerboard
     * @param damage number of damage to give to a player.
     * @param color Color of the attacker.
     */
    public abstract void addDamage(int damage, Color color);

    /**
     * Adds marks to a player's playerboard.
     * @param marks number of marks to add.
     * @param color color of the marks to add.
     */
    public abstract void addMarks(int marks, Color color);

    /**
     * Changes a player's position on the map.
     * @param square destination square.
     */
    public abstract void setPosition(SquareAbstract square);

    public  abstract PlayerAbstract getJustDamagedBy();

    public abstract void setJustDamagedBy(PlayerAbstract playerAbstract);

    /**
     * Decreases a players ammo.
     * @param cost ammo to remove.
     */
    public abstract void pay(Cost cost);

    /**
     * Method that is used to decrease a cost of a certain effect or weapon
     * if the client decides to use ammos to pay.
     * @param cost initial cost to pay.
     * @param powerUpCards List of powerup chosen by the player.
     */
    public abstract void payWithPowerUps(Cost cost, List<PowerUpCard> powerUpCards);

    public abstract boolean hasPowerUpCards(List<PowerUpCard> powerUpCards);

    public abstract boolean hasWeaponCards(List<WeaponCard> weaponCards);

    /**
     * method that processes a player's death
     */
    public abstract void die();

    public abstract boolean isOverkilled();

    public abstract Color getKillerColor();

    /**
     * Method used to check if a player is connected or not to the server.
     * It's used to check if a player has to be skipped.
     * @return returns true if the player is connected.
     */
    public abstract boolean isActive();

    public abstract void setActive(boolean active);

    /**
     * Adds point to a player during the point distribution phase.
     * @param i number of points to add.
     */
    public abstract void addPoints(int i);

    /**
     * Method used when a player is not active and needs to respawn.
     * @return A random powerup from a player's hand.
     */
    public abstract PowerUpCard getRandomPowerupCard();

    /**
     * Method used to know if a player can play a tagback grenade when shot.
     * @return number of tagback in hand.
     */
    public abstract int getNumberOfTagbacks();

    /**
     * Method used to know if a certain player can play a tagback grenade.
     * It check if the player has just been shot.
     * @param currentPlayer player currently playing, he is the only one who can inflict damage.
     * @return returns true if the player has been damaged by currentplayer
     */
    public abstract boolean canPlayTagback(PlayerAbstract currentPlayer);

    public abstract PlayerState getStateAfterDeath();

    public abstract String printOnCli();
}