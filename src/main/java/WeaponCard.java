
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
        aim = weapon.aim(); //questa è la lista di personaggi a cui posso sparare
        weapon.charge();


        String s;
        //l'utente dovrà scegliere tramite questo metodo il giocatore che vuole attaccare. Potrebbe per esempio inserire un intero a indicare l'indice della lista
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


        //questo mi restituisce l'indice di aim che a me interessa
        weapon.shoot(aim.get(Integer.valueOf(s))); //in qualche modo fa scegliere allo user la persona a cui vuole sparare
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