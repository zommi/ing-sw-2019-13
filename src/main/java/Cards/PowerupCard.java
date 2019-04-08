package Cards;

import Cards.CardInterface;
import Cards.Powerup;


public class PowerupCard implements CardInterface {

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




    public void play() {
        powerup.usePowerup();
        return;
    }




    public void draw() {
        if(powerupDeck.isEmpty()) //If it's empty, the deck need to be recharged.
            powerupDeck.restore();
        powerupDeck.draw();
        }
        return;
    }




    public void getEffect() {
        // TODO implement here
        return null;
    }



    public void getName() {
        // TODO da file
        return null;
    }



    public void discard() {
        powerupDeck.discard(this); //It will add the card to the discardedcards.
        return null;
    }

}