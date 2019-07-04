package client.info;

import server.model.cards.PowerUpCard;

public class SpawnInfo implements Info {

    private PowerUpCard powerupCard;

    public SpawnInfo(PowerUpCard powerupCard){
        this.powerupCard = powerupCard;
    }

    public PowerUpCard getPowerupCard() {
        return powerupCard;
    }
}
