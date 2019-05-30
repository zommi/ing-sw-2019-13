package server.model.cards;

import constants.*;
import exceptions.InvalidMoveException;
import server.model.gameboard.*;
import server.model.items.AmmoTileItem;

import java.io.Serializable;


public class PowerupCard implements CardInterface, AmmoTileItem, Serializable {

    private Color color;
    private Powerup powerup;
    private PowerupDeck powerupDeck;


    public PowerupCard(Color color, Powerup powerup, PowerupDeck powerupDeck) {
        this.color = color;
        this.powerup = powerup;
        this.powerupDeck = powerupDeck;
    }

    public Powerup getPowerUp(){
        return this.powerup;
    }

    public Color useAsAmmoCube() { //This method returns a color. Then the player will modify his attributes.
        return(color);
    }

    public void draw() {
        powerupDeck.draw();
    }

    public Color getColor() {
        return color;
    }

    public void getEffect() {
        // TODO implement here
    }

    public void play(){ //TODO!!!!!
    };

    public String getName() {
        return powerup.getName();
    }

    public void discard() {
        powerupDeck.discardCard(this); //It will add the card to the discardedcards.
    }

}