package GameBoard;

import Cards.*;
import Constants.Constants;

import java.util.*;

/**
 * 
 */
public class PowerupDeck {

    private LinkedList<PowerupCard> deck;
    public List<PowerupCard> discardedCards;
    
    public PowerupDeck() {
        this.discardedCards = new ArrayList<PowerupCard>();
        initializeDeck();
    }


    //TODO once done powerup cards finish this
    public void initializeDeck(){
        this.deck = new LinkedList<PowerupCard>();
        for(int i = 0; i < Constants.NUMBER_OF_POWERUP_CARDS; i++){
        }
    }


    public void shuffle() {
        Collections.shuffle(this.deck);
    }

    public boolean isEmpty() {
        return this.deck.isEmpty();
    }

    public void restore() {
        for(PowerupCard card : discardedCards){
            this.deck.add(card);
            this.discardedCards.remove(card);
        }
    }

    public void discardCard(PowerupCard card) {
        this.discardedCards.add(card);
    }

    public PowerupCard draw() {
        return this.deck.pop();
    }
}