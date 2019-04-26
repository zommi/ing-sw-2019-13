package Model.GameBoard;

import Model.Cards.WeaponCard;
import Constants.Constants;
import Model.Map.*;
import java.util.*;


public class WeaponDeck {

    private LinkedList<WeaponCard> deck;

    public WeaponDeck() {
        initializeDeck();
    }

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

    public WeaponCard draw() {
        return this.deck.pop();
    }

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


    public WeaponCard getTop(){
        return this.deck.get(0);
    }

}