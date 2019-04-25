package GameBoard;

import Cards.WeaponCard;
import Constants.Constants;
import Map.*;
import java.util.*;


public class WeaponDeck {

    private int size;
    private LinkedList<WeaponCard> deck;

    public WeaponDeck() {
        this.size = Constants.NUMBER_OF_WEAPONS;
        this.deck = initializeDeck();
    }

    private LinkedList<WeaponCard> initializeDeck() {
        LinkedList<WeaponCard> result = new LinkedList<WeaponCard>();
        for(int i = 0; i < Constants.NUMBER_OF_WEAPONS; i++){
            result.add(new WeaponCard(i,this));
        }
        return result;
    }


    public int getSize() {
        return size;
    }

    public void draw(SpawnPoint spawn) {
        this.size = this.size - 1;
        spawn.addItem(this.deck.pop());
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


    public String getTop(){
        return this.deck.get(0).getName();
    }

}