
import java.util.*;

/**
 * 
 */
public class WeaponCard implements CollectableInterface implements CardInterface {

    private List<AmmoCube> cost;
    private String name;
    private Weapon weapon;
    private List<Character> aim;
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


    public void getName() {
        return name;
    }

    public void play() {

        aim = weapon.aim(); //questa è la lista di personaggi a cui posso sparare
        weapon.charge();
        weapon.shoot(weapon.choose(aim)); //in qualche modo fa scegliere allo user la persona a cui vuole sparare
    }


    public void draw() {
        //mi serve un attibuto size per il mazzo
        if(weaponDeck.getSize() > 0)
            weaponDeck.draw();
        else
            //stampo che non ci son piu carte da pescare dal mazzo.
        return;
    }


    public void getEffect() {
        // da leggere da file
        return;
    }


    @Override
    public void display() {
        //da mostrare GUI
    }
}