package GameBoard;

import Cards.*;
import Constants.Constants;

import java.io.File;
import java.io.IOException;
import java.util.*;

import Constants.*;
import Exceptions.InvalidMoveException;
import Exceptions.ReadJsonErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
public class PowerupDeck {

    private LinkedList<PowerupCard> deck;
    private List<PowerupCard> discardedCards;

    public PowerupDeck() {
        this.discardedCards = new ArrayList<>();
        try {
            initializeDeck();
        }catch (ReadJsonErrorException e){
            e.printStackTrace();
        }
    }


    //There are 24 powerup cards, so 4powerups*3colors*2cardsForType=24
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
        for(int i = 0; i < arrayOfPowerups.length; i++){
            for(int j = 0; j < arrayOfPowerups[i].getNumberOfCards(); j++){
                this.deck.push(new PowerupCard(
                        arrayOfPowerups[i].getValue(),
                        getPoweupFromIndex(arrayOfPowerups[i].getIndex()),
                        this
                ));
            }
        }
        shuffle();
    }

    private Powerup getPoweupFromIndex(int index) {
        switch (index){
            case 0: return new TargetingScope();
            case 1: return new Newton();
            case 2: return new Teleporter();
            case 3: return new TagbackGrenade();
            default: return null;
        }
    }


    public void shuffle() {
        Collections.shuffle(this.deck);
    }

    public boolean isEmpty() {
        return this.deck.isEmpty();
    }

    public void restore(){
        if(deck.isEmpty()) {
            this.deck.addAll(this.discardedCards);
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