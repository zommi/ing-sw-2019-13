package GameBoard;

import Cards.*;
import Constants.Constants;

import java.util.*;

import Constants.*;
import Exceptions.InvalidMoveException;

/**
 * 
 */
public class PowerupDeck {

    private LinkedList<PowerupCard> deck;
    private List<PowerupCard> discardedCards;

    public PowerupDeck() {
        this.discardedCards = new ArrayList<>();
        initializeDeck();
    }


    //There are 24 powerup cards, so 4powerups*3colors*2cardsForType=24
    public void initializeDeck(){
        int colorIndex = 0;
        int numberOfCardsPerType = Constants.NUMBER_OF_POWERUP_CARDS / 4;
        this.deck = new LinkedList<>();
        for(int i = 0; i < numberOfCardsPerType; i++){
            if(i%2 == 0 && i != 0)colorIndex++;
            this.deck.add(new PowerupCard(Color.fromIndex(colorIndex),new TargetingScope(),this));
            this.deck.add(new PowerupCard(Color.fromIndex(colorIndex),new Newton(),this));
            this.deck.add(new PowerupCard(Color.fromIndex(colorIndex),new Teleporter(),this));
            this.deck.add(new PowerupCard(Color.fromIndex(colorIndex),new TagbackGrenade(),this));
        }
        shuffle();
    }


    public void shuffle() {
        Collections.shuffle(this.deck);
    }

    public boolean isEmpty() {
        return this.deck.isEmpty();
    }

    public void restore(){
        if(deck.isEmpty()) {
            for (PowerupCard card : discardedCards) {
                this.deck.add(card);
                this.discardedCards.remove(card);
            }
            shuffle();
        }
    }

    public void discardCard(PowerupCard card) {
        this.discardedCards.add(card);
    }

    public PowerupCard draw(){
        if(this.deck.isEmpty())restore();
        return this.deck.pop();
    }

    public List<PowerupCard> getDeck() {
        return deck;
    }

    public List<PowerupCard> getDiscardedCards() {
        return discardedCards;
    }

    @Override
    public String toString() {
        String stringToReturn = "Cards in deck:\n";
        for(PowerupCard card : deck){
            stringToReturn += card.getName() + '(' + card.getColor() + ')' + '\n';
        }
        stringToReturn += "Cards discarded:\n";
        for(PowerupCard card : discardedCards){
            stringToReturn += card.getName() + '(' + card.getColor() + ')' + '\n';
        }
        return stringToReturn;
    }
}