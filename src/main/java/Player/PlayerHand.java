package Player;

import Cards.*;
import Exceptions.InvalidMoveException;


import java.util.*;

/**
 * 
 */
public class PlayerHand {

    private ArrayList<CardInterface> weaponHand;
    private ArrayList<CardInterface> powerupHand;
    private ConcretePlayer player;

    /**
     *
     */
    public PlayerHand(ConcretePlayer p) {
        this.player = p;
        this.weaponHand = new ArrayList<>();
        this.powerupHand = new ArrayList<>();
    }


    /**
     * @return
     */
    public List<WeaponCard> getWeapons() {
        return (ArrayList<WeaponCard>) weaponHand.clone();
    }

    /**
     * @return
     */
    public List<PowerupCard> getPowerups() {
        return (ArrayList<PowerupCard>) powerupHand.clone();
    }

    /**
     * @return
     * @param choice
     */
    public void playCard(int choice, char c) throws InvalidMoveException {
        System.out.println("Playing Card...");
        switch (c){
            //case 'w' : weaponHand.get(choice).play(); //which is a weaponcard
                //break;
            //case 'p' : powerupHand.get(choice).play(); //which is a powerupcard
                //break;
            default: throw new InvalidMoveException();
        }
    }

    @Override
    public String toString() {
        return "Weapons: " + weaponHand.toString() + "\n" + "Powerups: " + powerupHand.toString();
    }


    public void addCard(WeaponCard card){
        weaponHand.add(card);
        //TODO add swap on gameboard if size == 3
    }

}