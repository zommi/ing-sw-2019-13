package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.ScopePack;
import client.weapons.Weapon;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;

import java.util.List;

/**
 * Class that abstracts the input from both the gui and cli with respect to shooting input
 */
public abstract class InputAbstract {

    GameModel gameModel;

    /**
     * Asks the player if he wants to reload.
     * @param weaponCard Card to reload.
     * @return ReloadInfo containing the data.
     */
    public abstract ReloadInfo askReload(WeaponCard weaponCard);

    /**
     * Asks the player if he wants to activate a given macro effect of the weapon
     * @param macroEffect The macro effect to ask
     * @return Choice of the player
     */
    public abstract boolean getChoice(MacroEffect macroEffect);

    /**
     * Ask the player what mode to use in case of two alternatives.
     * @param weapon weapon chosen by the player.
     * @return the macro effect chosen between the two alternatives
     */
    public abstract MacroEffect chooseOneMacro(Weapon weapon);

    /**
     * Asks the player if he wants to activate a given micro effect of the weapon
     * @param microEffect The micro effect to ask
     * @return Choice of the player
     */
    public abstract boolean getChoice(MicroEffect microEffect);

    /**
     * Asks the player for a square.
     * @param maxSquares the number of squares to ask.
     * @return list containing all the squares chosen by the player
     */
    public abstract List<SquareInfo> askSquares(int maxSquares);

    /**
     * Asks the player the target he wants to attack.
     * @param maxTargetPlayerSize max number of player that can be targeted.
     * @return list containing the names of the players chosen.
     */
    public abstract List<String> askPlayers(int maxTargetPlayerSize);

    /**
     * asks the attacker what rooms he wants to target.
     * @param maxTargetRoomSize max number of rooms to target.
     * @return list containing the name of the rooms targeted
     */
    public abstract List<String> askRooms(int maxTargetRoomSize);

    /**
     * asks the player if he wants to move. This can be asked either because
     * it's an effect of the weapon or because the state of the player allows it.
     * @return decision made by the player.
     */
    public abstract boolean getMoveChoice();

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * Asks the player if he wants to use powerups to pay for additional effects.
     * @return list of powerups to use.
     */
    public abstract List<PowerUpCard> askPowerUps();

    /**
     * Asks the player if he wants to use targeting scopes while shooting.
     * @return list of all the targeting scope used.
     */
    public abstract List<ScopePack> askTargetingScopes();
}
