package Cards;

import Constants.*;
import GameBoard.*;


public class PowerupCard implements CardInterface {

    private Color color;
    private Powerup powerup;
    private PowerupDeck powerupDeck;
    String name;


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
        if(powerupDeck.isEmpty()) //If it's empty, the deck need to be recharged.
            powerupDeck.restore();
        powerupDeck.draw();
    }





    public void getEffect() {
        // TODO implement here
    }



    public String getName() {
        return name;
    }



    public void discard() {
        powerupDeck.discardCard(this); //It will add the card to the discardedcards.
    }

}