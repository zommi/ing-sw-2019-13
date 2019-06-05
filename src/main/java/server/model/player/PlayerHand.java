package server.model.player;

import constants.Constants;
import server.controller.Controller;
import server.model.cards.*;


import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class PlayerHand implements Serializable {

    private ArrayList<WeaponCard> weaponHand;
    private ArrayList<PowerUpCard> powerupHand;
    private transient ConcretePlayer player;

    /**
     *
     */

    public PlayerHand(){
    }

    public PlayerHand(ConcretePlayer p) {
        this.player = p;
        this.weaponHand = new ArrayList<>();
        this.powerupHand = new ArrayList<>();
    }

    public ConcretePlayer getPlayer(){
        return this.player;
    }


    public List<WeaponCard> getWeaponHand() {
        return (ArrayList<WeaponCard>) weaponHand.clone();  //cloning for server answers
    }

    public void removePowerUpCard(PowerUpCard powerupCard){
        for(int i = 0; i < powerupHand.size(); i++){
            if((powerupHand.get(i).getColor() == powerupCard.getColor())&&(powerupHand.get(i).getName().equals(powerupCard.getName())))
                this.powerupHand.remove(i);
        }
    }

    public List<PowerUpCard> getPowerupHand() {
        return powerupHand;    //cloning for server answers
    }

    /*
    public void playWeapon(int choice, int extra, int x, int y){
        weaponHand.get(choice).play(extra, x, y);
    }
    */

    public void playPowerup(int choice){
        powerupHand.get(choice).play();
    }

    @Override
    public String toString() {
        return "Weapons: " + weaponHand.toString() + "\n" + "Powerups: " + powerupHand.toString();
    }


    public void addCard(WeaponCard cardToAdd){
        weaponHand.add(cardToAdd);
        //TODO add swap on gameboard if size == 3
        //note that on spawnpoints there is a list of the weapons currently available
        //i just need to add the weapon the player decides to discard
    }

    public void addCard(WeaponCard cardToAdd, WeaponCard cardToLeave, Controller controller){
        this.weaponHand.add(cardToAdd);
        this.weaponHand.remove(cardToLeave);
        //TODO add swap on gameboard if size == 3
        //note that on spawnpoints there is a list of the weapons currently available
        //i just need to add the weapon the player decides to discard
        this.player.getGameCharacter().getPosition().addItem(cardToLeave);
        this.player.getGameCharacter().getPosition().removeItem(cardToAdd, controller);
    }

    public boolean weaponFull(){return this.weaponHand.size() == Constants.MAX_NUMBER_OF_CARDS;}

    public boolean powerupsFull(){return this.powerupHand.size() == Constants.MAX_NUMBER_OF_CARDS;}

    public void addCard(PowerUpCard draw) {
        if(this.powerupHand.size()< Constants.MAX_NUMBER_OF_CARDS)
            this.powerupHand.add(draw);
    }

}