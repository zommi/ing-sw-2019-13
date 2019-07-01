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
    private int points;


    public PlayerHand(ConcretePlayer p) {
        this.player = p;
        this.weaponHand = new ArrayList<>();
        this.powerupHand = new ArrayList<>();
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int i){
        points += i;
    }

    public ConcretePlayer getPlayer(){
        return this.player;
    }


    public List<WeaponCard> getWeaponHand() {
        return weaponHand;
    }

    public List<PowerUpCard> getTargetingScopes(){
        List<PowerUpCard> returnList = new ArrayList<>();
        for(PowerUpCard powerUpCard : powerupHand){
            if(powerUpCard.getName().equalsIgnoreCase(Constants.TARGETING_SCOPE))
                returnList.add(powerUpCard);
        }
        return returnList;
    }

    public void removePowerUpCard(PowerUpCard powerUpCard){
        Iterator<PowerUpCard> iterator = powerupHand.iterator();
        PowerUpCard powerUpCard1;
        while(iterator.hasNext()){
            powerUpCard1 = iterator.next();
            if(powerUpCard.equals(powerUpCard1)){
                iterator.remove();
                player.getPlayerBoard().decreasePowerUpHandSize();
                return;
            }

        }
    }

    public void removeWeaponCard(WeaponCard weaponCard){
        Iterator<WeaponCard> iterator = weaponHand.iterator();
        WeaponCard weaponCard1;
        while(iterator.hasNext()){
            weaponCard1 = iterator.next();
            if(weaponCard.equals(weaponCard1)){
                iterator.remove();
                player.getPlayerBoard().decreaseWeaponHandSize();
                return;
            }

        }
    }



    public List<PowerUpCard> getPowerupHand() {
        return powerupHand;    //cloning for server answers
    }

    public void playPowerup(int choice){
        powerupHand.get(choice).play();
    }

    @Override
    public String toString() {
        return "Weapons: " + weaponHand.toString() + "\n" + "Powerups: " + powerupHand.toString();
    }


    public void addCard(WeaponCard cardToAdd){
        weaponHand.add(cardToAdd);
        player.getPlayerBoard().increaseWeaponHandSize();
    }

    public boolean powerupsFull(){return this.powerupHand.size() == Constants.MAX_POWERUP_HAND;}

    public void addCard(PowerUpCard draw) {
        System.out.println("Adding one card");
        this.powerupHand.add(draw);
        player.getPlayerBoard().increasePowerUpHandSize();
    }

    public boolean areAllWeaponsLoaded(){
        for(WeaponCard weaponCard : weaponHand){
            if(!weaponCard.isReady())
                return false;
        }
        return true;
    }

    public boolean areAllWeaponsUnloaded(){
        for(WeaponCard weaponCard : weaponHand){
            if(weaponCard.isReady())
                return false;
        }
        return true;
    }

    public int getNumberOfTagbacks() {
        int counter = 0;
        for(PowerUpCard powerUpCard : powerupHand){
            if(powerUpCard.getName().equalsIgnoreCase("Tagback Grenade"))
                counter++;
        }
        return counter;
    }
}