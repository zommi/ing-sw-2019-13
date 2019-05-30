package client;

import server.model.cards.PowerupCard;

public class SpawnInfo implements Info {

    PowerupCard powerupCard;

    public SpawnInfo(PowerupCard powerupCard){
        this.powerupCard = powerupCard;
    }

    public PowerupCard getPowerupCard() {
        return powerupCard;
    }
}
