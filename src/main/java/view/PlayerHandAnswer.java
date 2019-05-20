package view;

import server.model.cards.Powerup;
import server.model.cards.PowerupCard;
import server.model.cards.WeaponCard;
import server.model.player.PlayerHand;
import java.util.List;

public class PlayerHandAnswer implements ServerAnswer {

    private List<WeaponCard> weaponHand;
    private List<PowerupCard> powerupHand;


    public PlayerHandAnswer(PlayerHand p){
        this.weaponHand = p.getWeapons();
        this.powerupHand = p.getPowerups();
    }

    public List<WeaponCard> getWeaponHand(){
        return this.weaponHand;
    }
    public List<PowerupCard> getPowerupHand(){
        return this.powerupHand;
    }
}
