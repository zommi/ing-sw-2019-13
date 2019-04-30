package server.model.gameboard;

import server.model.cards.*;
import constants.Constants;

import java.io.File;
import java.io.IOException;
import java.util.*;

import exceptions.ReadJsonErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
public class PowerupDeck {

    /**
     * deck of cards that can be drawn
     */
    private LinkedList<PowerupCard> deck;

    /**
     * stack of discarded cards
     */
    private List<PowerupCard> discardedCards;

    /**
     * default constructor
     */
    public PowerupDeck() {
        this.discardedCards = new ArrayList<>();
        try {
            initializeDeck();
        }catch (ReadJsonErrorException e){
            e.printStackTrace();
        }
    }

    /**
     * Method that reads a JSON file containing all the data about the powerup cards
     * and initializes the deck
     * @throws ReadJsonErrorException if the method fails to read the JSON file
     */
    public void initializeDeck() throws ReadJsonErrorException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(Constants.PATH_TO_POWERUP_JSON);

        Powerup[] arrayOfPowerups = new Powerup[0];

        try {
            arrayOfPowerups = mapper.readValue(file, Powerup[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(arrayOfPowerups.length == 0) throw new ReadJsonErrorException();

        this.deck = new LinkedList<>();
        try {
            for (int i = 0; i < arrayOfPowerups.length; i++) {
                for (int j = 0; j < arrayOfPowerups[i].getNumberOfCards(); j++) {
                    this.deck.push(new PowerupCard(
                            arrayOfPowerups[i].getValue(),
                            getPoweupFromIndex(arrayOfPowerups[i].getIndex()),
                            this
                    ));
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        shuffle();
    }

    /**
     * auxiliary function called during the initialization phase that returns a new powerup
     * given a index
     * @param index int between 0 and 3 that corresponds to a powerup
     * @return a new powerup
     */
    private Powerup getPoweupFromIndex(int index) {
        switch (index){
            case 0: return new TargetingScope();
            case 1: return new Newton();
            case 2: return new Teleporter();
            case 3: return new TagbackGrenade();
            default: return null;
        }
    }


    /**
     * shuffles the deck
     */
    public void shuffle() {
        Collections.shuffle(this.deck);
    }

    public boolean isEmpty() {
        return this.deck.isEmpty();
    }

    /**
     * if the deck is empty move all the cards from the discarded pile to the deck
     */
    public void restore(){
        if(deck.isEmpty()) {
            this.deck.addAll(this.discardedCards);
            this.discardedCards.clear();
            shuffle();
        }
    }

    /**
     * when a card is used it is put in the discarded pile
     * @param card reference to the card to discard
     */
    public void discardCard(PowerupCard card) {
        this.discardedCards.add(card);
    }

    /**
     *
     * @return the first card on the deck
     */
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
        String stringToReturn = "cards in deck:\n";
        for(PowerupCard card : deck){
            stringToReturn += card.getName() + '(' + card.getColor() + ')' + '\n';
        }
        stringToReturn += "cards discarded:\n";
        for(PowerupCard card : discardedCards){
            stringToReturn += card.getName() + '(' + card.getColor() + ')' + '\n';
        }
        return stringToReturn;
    }
}