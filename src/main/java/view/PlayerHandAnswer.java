package view;

import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.player.PlayerHand;

import java.util.List;


public class PlayerHandAnswer implements ServerAnswer {

    private PlayerHand playerHand;

    public PlayerHandAnswer(PlayerHand p){
        this.playerHand = p;
    }

    public PlayerHand getPlayerHand() {
        return this.playerHand;
    }

    public List<WeaponCard> getWeaponHand(){
        return playerHand.getWeaponHand();
    }

    public List<PowerUpCard> getPowerupHand(){
        return playerHand.getPowerupHand();
    }

}
