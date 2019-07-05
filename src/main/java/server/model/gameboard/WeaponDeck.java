package server.model.gameboard;

import client.weapons.Cost;
import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ReadJsonErrorException;
import client.weapons.Weapon;
import server.model.cards.WeaponCard;
import constants.Constants;

import java.io.*;
import java.util.*;


public class WeaponDeck implements Serializable {

    /**
     * queue of weapon cards
     */
    private LinkedList<WeaponCard> deck;

    /**
     * List of weapon cards that is never updated.
     */
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
        InputStream weaponStream = getClass().getResourceAsStream(Constants.PATH_TO_WEAPONS_JSON);
        BufferedReader reader = new BufferedReader(new InputStreamReader(weaponStream));

        Weapon[] arrayOfWeapons = new Weapon[0];

        try{
            arrayOfWeapons = mapper.readValue(reader, Weapon[].class);
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
                    if(microEffect.getDescription() == null)
                        microEffect.setDescription("");
                    microEffect.setNumber(micro);
                    microEffect.setMacroNumber(macro);
                    micro++;
                }
                macro++;
            }
        }

        this.deck = new LinkedList<>();
        this.staticDeck = new LinkedList<>();

        int i = 0;
        for(Weapon weapon : arrayOfWeapons){
            WeaponCard weaponCard = new WeaponCard(weapon);
            weaponCard.setId(i);
            this.deck.push(weaponCard);
            this.staticDeck.push(weaponCard);
            i++;

        }

        this.shuffle();
    }


    public int getSize() {
        return this.deck.size();
    }

    /**
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
     * @return the first card in the queue without removing it
     */
    public WeaponCard getTop(){
        return this.deck.get(0);
    }

    public Weapon getWeapon(String weaponName){
        return getWeaponCard(weaponName).getWeapon();
    }

    public WeaponCard getWeaponCard(String weaponName){
        for(WeaponCard weaponCard : staticDeck){
            if(weaponCard.getWeapon().getName().equalsIgnoreCase(weaponName))
                return weaponCard;

        }
        return null;
    }
}