package client.powerups;

import client.info.Info;
import client.info.SquareInfo;
import server.model.cards.PowerUpCard;

public class PowerUpPack implements Info {
    private PowerUpCard powerUpCard;
    private SquareInfo squareInfo;
    private String target;

    public PowerUpPack(PowerUpCard powerUpCard, SquareInfo squareInfo, String target){
        this.powerUpCard = powerUpCard;
        this.squareInfo = squareInfo;
        this.target = target;
    }

    public SquareInfo getSquareInfo() {
        return squareInfo;
    }

    public String getTarget() {
        return target;
    }

    public PowerUpCard getPowerupCard() {
        return powerUpCard;
    }
}
