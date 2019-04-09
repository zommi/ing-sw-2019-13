package Cards;

import GameBoard.WeaponDeck;
import Items.*;
import Map.*;

import java.util.*;

/**
 * 
 */
public class WeaponCard implements CollectableInterface, CardInterface {

    private List<AmmoCube> cost;
    private String name;
    private Weapon weapon;
    private WeaponDeck weaponDeck;
    /**
     * Initialize the card cost
     */
    public WeaponCard(List<AmmoCube> cardcost, String namecard, Weapon weap, WeaponDeck weapdeck) {
        this.cost = cardcost;
        this.name = namecard;
        this.weapon = weap;
        this.weaponDeck = weapdeck;
    }


    public List<AmmoCube> getCost() {
        return cost;
    }


    public String getName() {
        return name;
    }

    public void discard() { }


    public ArrayList<ArrayList<Character>> chooseCharacter(Square square){
        return(weapon.getCommand().execute(square));    //this method returns the list of the possible targets
    }


    public Bullet play() {
        return weapon.shoot();
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