package view;

import server.model.cards.PowerupCard;
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

    public ArrayList<PowerupCard> getPowerupHand(){
        return (ArrayList<PowerupCard>) playerHand.getPowerupHand();
    }

}
