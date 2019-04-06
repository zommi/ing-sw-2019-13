
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 
 */
public class WeaponCard implements CollectableInterface implements CardInterface {

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


    public void getName() {
        return name;
    }




    public void play() {
        ArrayList<ArrayList<Character>> aim;
        aim = weapon.aim(); //This returns the list of characters I can shoot
        weapon.charge();


        String s;
        //The user will have to choose the character he wants to shoot. He could for example insert an int to indicate the index of the list.
        System.out.println("Enter the index of the character you want to shoot : ");

        try{
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            s = bufferRead.readLine();

            System.out.println("You chose to shoot character number :  " s);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }


        //This returns the index of aim I am interested to
        weapon.shoot(aim.get(Integer.valueOf(s)));
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


    @Override
    public void display() {
        // TODO
    }
}