package view;

import server.model.player.PlayerHand;


public class PlayerHandAnswer implements ServerAnswer {

    private PlayerHand playerHand;

    public PlayerHandAnswer(PlayerHand p){
        this.playerHand = p.createCopy(p);
    }

    public PlayerHand getplayerHand() {
        return this.playerHand;
    }

}
