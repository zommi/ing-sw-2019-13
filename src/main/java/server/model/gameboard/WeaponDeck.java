package server.model.gameboard;

import server.model.cards.WeaponCard;
import constants.Constants;

import java.util.*;


public class WeaponDeck {

    /**
     * queue of weapon cards
     */
    private LinkedList<WeaponCard> deck;

    /**
     * defaul constructor
     */
    public WeaponDeck() {
        initializeDeck();
    }

    /**
     * initializes the deck by creating all the different cards
     */
    private void initializeDeck() {
        this.deck = new LinkedList<WeaponCard>();
        for(int i = 0; i < Constants.NUMBER_OF_WEAPONS; i++){
            this.deck.add(new WeaponCard(i,this));
        }
        shuffle();
    }


    public int getSize() {
        return this.deck.size();
    }

    /**
     *
     * @return the first card in the deck
     */
    public WeaponCard draw() {
        return this.deck.pop();
    }

    /**
     * shuffles the deck
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    @Override
    public String toString() {
        String stringToReturn = "";
        for(WeaponCard card : deck){
            stringToReturn = stringToReturn + card.getName() + " - ";
        }
        return stringToReturn;
    }

    /**
     *
     * @return the first card in the queue without removing it
     */
    public WeaponCard getTop(){
        return this.deck.get(0);
    }

}