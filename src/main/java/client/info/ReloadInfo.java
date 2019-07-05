package client.info;

import client.weapons.Cost;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;

import java.util.List;

/**
 * Info: send to the server an action to reload the weapons
 */
public class ReloadInfo implements Info{
    /**
     * List of weapon cards to reload
     */
    private List<WeaponCard> weaponCards;
    /**
     * List of powerup cards to pay with
     */
    private List<PowerUpCard> powerUpCards;
    /**
     * Cost of the reload action
     */
    private Cost cost;

    public ReloadInfo(List<WeaponCard> weaponCards, List<PowerUpCard> powerUpCards){
        this.weaponCards = weaponCards;
        this.powerUpCards = powerUpCards;
    }

    public void setPowerUpCards(List<PowerUpCard> powerUpCards) {
        this.powerUpCards = powerUpCards;
    }

    public List<PowerUpCard> getPowerUpCards() {
        return powerUpCards;
    }

    public void setWeaponCards(List<WeaponCard> weaponCards) {
        this.weaponCards = weaponCards;
    }

    public List<WeaponCard> getWeaponCards() {
        return weaponCards;
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }
}
