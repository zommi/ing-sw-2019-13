package server.model.gameboard;

import client.weapons.Cost;
import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ReadJsonErrorException;
import client.weapons.Weapon;
import server.model.cards.WeaponCard;
import constants.Constants;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;


public class WeaponDeck implements Serializable {

    /**
     * queue of weapon cards
     */
    private LinkedList<WeaponCard> deck;
    private LinkedList<WeaponCard> staticDeck;

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
                if(macroEffect.getCost()==null)
                    macroEffect.setCost(new Cost(0,0,0));
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
        this.staticDeck = new LinkedList<>();

        for(Weapon weapon : arrayOfWeapons){
            this.deck.push(new WeaponCard(weapon));
            this.staticDeck.push(new WeaponCard(weapon));

        }

        this.shuffle();

        /*for(int i = 0; i < arrayOfWeapons.length-1; i++){
            this.deck.push(new WeaponCard(arrayOfWeapons[i]));
            this.staticDeck.push(new WeaponCard(arrayOfWeapons[i]));

        }
        Weapon weapon = null;
        for(Weapon weapon1: arrayOfWeapons)
            if(weapon1.getName().equals("Shockwave"))
                weapon = weapon1;

        this.deck.push(new WeaponCard(weapon));
        this.staticDeck.push(new WeaponCard(weapon));*/

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
        Collections.shuffle(this.deck);
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
        for(WeaponCard weaponCard : staticDeck){
            if(weaponCard.getWeapon().getName().equalsIgnoreCase(weaponName))
                return weaponCard.getWeapon();

        }
        return null;
    }

    public WeaponCard getWeaponFromIndex(int index) {
        return this.deck.get(index);
    }

}