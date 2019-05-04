package server.model.cards;

import server.model.items.*;
import server.model.map.*;
import server.model.player.GameCharacter;

import java.util.*;

/**
 * 
 */
public class WeaponCard implements CollectableInterface, CardInterface {

    private List<AmmoCube> cost;
    private String name;
    private Weapon weapon;
    private boolean ready;
    private int weaponIndex;

    /**
     * Initialize the card cost
     */
    public WeaponCard(int indexWeapon) {
        this.weapon = new Weapon(indexWeapon);
        this.cost = this.weapon.getCost();
        this.name = this.weapon.getName();
        this.ready = true;
        this.weaponIndex = indexWeapon;
    }


    public List<AmmoCube> getCost() {
        return cost;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }

    public String getName() {
        return name;
    }

    public void discard() { }


    public ArrayList<ArrayList<GameCharacter>> chooseCharacter(SquareAbstract square, int extra){
        if(extra == 0)
            return(weapon.getCommand().execute(square));    //this method returns the list of the possible targets
        else if(extra == 1)
            return(weapon.getCommand1().execute(square));
        else //if(extra == 2)
            return(weapon.getCommand2().execute(square));
    }


    public ArrayList<Bullet> play(int extra, int x, int y) {
        return weapon.shoot(extra, x, y);
        //it calls SHOOT on weapon
    }




    public void draw() {
        //Draw card from spawnpoint.
    }


    public void getEffect() {
        // TODO READ FROM FILE
        return;
    }


    public void collect() {

    }

    public WeaponCard clone(){
        return new WeaponCard(this.weaponIndex);
    }

    @Override
    public String toString() {
        return this.name;
    }
}