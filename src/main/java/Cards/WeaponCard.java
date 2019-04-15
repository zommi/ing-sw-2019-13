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
    public WeaponCard(List<AmmoCube> cardcost, String namecard, Weapon weap, WeaponDeck weapdeck) {
        this.cost = cardcost;
        this.name = namecard;
        this.weapon = weap;
        this.weaponDeck = weapdeck;
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


    public Bullet play(int extra) {
        return weapon.shoot(extra);
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