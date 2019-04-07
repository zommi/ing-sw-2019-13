
import java.util.*;
import java.util.logging.Logger;

/**
 * 
 */
public class PlayerHand {

    private ArrayList<CardInterface> weaponHand;
    private ArrayList<CardInterface> powerupHand;
    private ConcretePlayer player;

    /**
     *
     */
    public PlayerHand(ConcretePlayer p) {
        this.player = p;
        this.weaponHand = new ArrayList<>();
        this.powerupHand = new ArrayList<>();
    }


    /**
     * @return
     */
    public List<WeaponCard> getWeapons() {
        return (ArrayList<WeaponCard>) weaponHand.clone();
    }

    /**
     * @return
     */
    public List<PowerupCard> getPowerups() {
        return (ArrayList<PowerupCard>) powerupHand.clone();
    }

    /**
     * @return
     * @param choice
     */
    public void playCard(int choice, char c) {
        System.out.println("Playing Card...");
        switch (c){
            case 'w' : weaponHand.get(choice).play();
                break;
            case 'p' : powerupHand.get(choice).play();
                break;
            default: System.err.println("UNEXPECTED CARD REQUEST!");
                break;
        }
    }

    @Override
    public String toString() {
        return "Weapons: " + weaponHand.toString() + "\n" + "Powerups: " + powerupHand.toString();
    }
}