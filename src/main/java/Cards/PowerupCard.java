package Cards;

import Constants.*;
import Exceptions.InvalidMoveException;
import GameBoard.*;
import Items.AmmoTileItem;


public class PowerupCard implements CardInterface, AmmoTileItem {

    private Color color;
    private Powerup powerup;
    private PowerupDeck powerupDeck;


    public PowerupCard(Color color, Powerup powerup, PowerupDeck powerupDeck) {
        this.color = color;
        this.powerup = powerup;
        this.powerupDeck = powerupDeck;
    }


    public Color useAsAmmoCube() { //This method returns a color. Then the player will modify his attributes.
        return(color);
    }




    public Bullet play() {
        return powerup.usePowerup();
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



    public String getName() {
        return powerup.getName();
    }



    public void discard() {
        powerupDeck.discardCard(this); //It will add the card to the discardedcards.
    }

}