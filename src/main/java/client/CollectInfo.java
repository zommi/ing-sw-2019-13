package client;

import constants.Constants;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;

import java.io.Serializable;
import java.util.List;

public class CollectInfo implements Serializable, Info {

    /**
     * Int containing the index of the weapon the player
     * wants to collect from the spawn point. If the player
     * wants to collect an ammo tile it is set to NO CHOICE
     */
    private int row;
    private int col;
    private int choice;
    private WeaponCard weaponToDiscard;
    private List<PowerUpCard> powerUpCards;

    /**
     * constructor used if the player doesn't specify a choice. It is used
     * when the player wants to collect an ammo tile
     */
    public CollectInfo(int row, int col, int choice, WeaponCard weaponToDiscard){
        this.choice = choice;
        this.row = row;
        this.col = col;
        this.weaponToDiscard = weaponToDiscard;
    }

    public List<PowerUpCard> getPowerUpCards() {
        return powerUpCards;
    }

    public void setPowerUpCards(List<PowerUpCard> powerUpCards) {
        this.powerUpCards = powerUpCards;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    public int getChoice() {
        return choice;
    }

    public WeaponCard getWeaponToDiscard() {
        return weaponToDiscard;
    }

}
