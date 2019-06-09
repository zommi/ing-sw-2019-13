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

    public boolean weaponFull(){return this.weaponHand.size() == Constants.MAX_WEAPON_HAND;}

    public boolean powerupsFull(){return this.powerupHand.size() == Constants.MAX_POWERUP_HAND;}

    public void addCard(PowerUpCard draw) {
        if(this.powerupHand.size()< Constants.MAX_POWERUP_HAND)
            this.powerupHand.add(draw);
    }

}