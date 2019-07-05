package client.info;

import server.model.cards.PowerUpCard;

/**
 * Info: send to the server an action to spawn a player
 */
public class SpawnInfo implements Info {

    /**
     * Powerup card that the player wants to discard
     */
    private PowerUpCard powerupCard;

    public SpawnInfo(PowerUpCard powerupCard){
        this.powerupCard = powerupCard;
    }

    public PowerUpCard getPowerupCard() {
        return powerupCard;
    }
}
