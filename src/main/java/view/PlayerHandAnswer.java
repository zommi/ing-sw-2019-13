package view;

import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.player.PlayerHand;

import java.util.ArrayList;


public class PlayerHandAnswer implements ServerAnswer {

    private PlayerHand playerHand;

    public PlayerHandAnswer(PlayerHand p){
        this.playerHand = p.createCopy(p);
    }

    public PlayerHand getplayerHand() {
        return this.playerHand;
    }

    public ArrayList<WeaponCard> getWeaponHand(){
        return (ArrayList<WeaponCard>) playerHand.getWeaponHand();
    }

    public ArrayList<PowerUpCard> getPowerupHand(){
        return (ArrayList<PowerUpCard>) playerHand.getPowerupHand();
    }

}
