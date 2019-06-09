package server.model.cards;

import constants.*;
import server.model.gameboard.*;
import server.model.items.AmmoTileItem;

import java.io.Serializable;


public class PowerUpCard implements CardInterface, AmmoTileItem, Serializable {

    private Color color;
    private PowerUp powerUp;
    private PowerupDeck powerupDeck;
    private String path;
    private int cardId;


    public PowerUpCard(Color color, PowerUp powerUp, String path, PowerupDeck powerupDeck, int cardId) {
        this.color = color;
        this.powerUp = powerUp;
        this.powerupDeck = powerupDeck;
        this.path = path;
        this.cardId = cardId;
    }

    public PowerUp getPowerUp() {
        return this.powerUp;
    }

    public Color useAsAmmoCube() { //This method returns a color. Then the player will modify his attributes.
        return (color);
    }

    public void draw() {
        powerupDeck.draw();
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return powerUp.getName();
    }

    public void discard() {
        powerupDeck.discardCard(this); //It will add the card to the discardedcards.
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PowerUpCard))
            return false;
        PowerUpCard card2 = (PowerUpCard) obj;

        return card2.cardId == this.cardId;

    }

    public String getPath() {
        return path;
    }

    public void play(){}

    @Override
    public String toString() {
        return color.getAnsi() + powerUp.getName() + Constants.ANSI_RESET;
    }
}