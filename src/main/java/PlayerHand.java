
import java.util.*;

/**
 * 
 */
public class PlayerHand {

    ArrayList<CardInterface> cardHand;
    ArrayList<CardInterface> powerupHand;

    /**
     * Default constructor
     */
    public PlayerHand() {
        cardHand = new ArrayList<>();
        powerupHand = new ArrayList<>();
    }


    /**
     * @return
     */
    public ArrayList<WeaponCard> getWeapons() {
        return (ArrayList<WeaponCard>) cardHand.clone();
    }

    /**
     * @return
     */
    public List<PowerupCard> getPowerups() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public void playCard() {
        // TODO implement here
        return null;
    }

}