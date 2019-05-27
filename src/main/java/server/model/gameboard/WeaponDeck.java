package server.model.gameboard;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ReadJsonErrorException;
import client.weapons.Weapon;
import server.model.cards.WeaponCard;
import constants.Constants;

import java.io.File;
import java.io.IOException;
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
        try {
            initializeDeck();
        } catch (ReadJsonErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * initializes the deck by creating all the different cards
     */
    private void initializeDeck() throws ReadJsonErrorException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(Constants.PATH_TO_WEAPONS_JSON);

        Weapon[] arrayOfWeapons = new Weapon[0];

        try{
            arrayOfWeapons = mapper.readValue(file, Weapon[].class);
        } catch (IOException e){
            e.printStackTrace();
        }

        if(arrayOfWeapons.length == 0) throw new ReadJsonErrorException();

        for(Weapon weapon : arrayOfWeapons){
            int macro = 0;
            for(MacroEffect macroEffect : weapon.getMacroEffects()){
                macroEffect.setNumber(macro);
                int micro = 0;
                for(MicroEffect microEffect : macroEffect.getMicroEffects()){
                    microEffect.setNumber(micro);
                    microEffect.setMacroNumber(macro);
                    micro++;
                }
                macro++;
            }
        }

        this.deck = new LinkedList<>();

        for(int i = 0; i < arrayOfWeapons.length; i++){
            this.deck.push(new WeaponCard(arrayOfWeapons[i]));
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

    public Weapon getWeapon(String weaponName){
        for(WeaponCard weaponCard : deck){
            if(weaponCard.getWeapon().getName().equals(weaponName))
                return weaponCard.getWeapon();

        }
        return null;
    }

    public WeaponCard getWeapon(int index) {
        return deck.get(index);
    }

}