package GameBoard;

import Cards.WeaponCard;
import Constants.Constants;

import java.util.*;


public class WeaponDeck {

    private int size;
    private List<WeaponCard> deck;

    public WeaponDeck() {
        this.size = Constants.NUMBER_OF_WEAPONS;
        this.deck = initializeDeck();
    }

    private List<WeaponCard> initializeDeck() {
        List<WeaponCard> result = new ArrayList<WeaponCard>();
        for(int i = 0; i < Constants.NUMBER_OF_WEAPONS; i++){
            result.add(new WeaponCard(i,this));
        }
        return result;
    }


    public int getSize() {
        return size;
    }

    public void draw() {
        this.size = this.size - 1;
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    @Override
    public String toString() {
        String stringToReturn = "";
        for(WeaponCard card : deck){
            stringToReturn = stringToReturn + card.getName() + "-";
        }
        return stringToReturn;
    }
}