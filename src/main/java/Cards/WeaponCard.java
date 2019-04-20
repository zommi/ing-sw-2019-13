package Cards;

import GameBoard.WeaponDeck;
import Items.*;
import Map.*;
import Player.Character;

import java.util.*;

/**
 * 
 */
public class WeaponCard implements CollectableInterface, CardInterface {

    private List<AmmoCube> cost;
    private String name;
    private Weapon weapon;
    private WeaponDeck weaponDeck;
    private boolean ready;

    /**
     * Initialize the card cost
     */
    public WeaponCard(int indexWeapon, WeaponDeck weaponDeck) {
        this.weapon = new Weapon(indexWeapon);
        this.cost = this.weapon.getCost();
        this.name = this.weapon.getName();
        this.weaponDeck = weaponDeck;
        this.ready = true;
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


    public ArrayList<ArrayList<Character>> chooseCharacter(SquareAbstract square){
        return(weapon.getCommand().execute(square));    //this method returns the list of the possible targets
    }


    public ArrayList<Bullet> play(int extra, int x, int y) {
        return weapon.shoot(extra, x, y);
        //it calls SHOOT on weapon
    }




    public void draw() {
        if(weaponDeck.getSize() > 0)
            weaponDeck.draw();
        else
            //The GUI will show that there are no more card to draw from the deck
        return;
    }


    public void getEffect() {
        // TODO READ FROM FILE
        return;
    }


    public void collect() {

    }
}