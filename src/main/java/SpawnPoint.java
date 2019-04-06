
import java.util.*;

/**
 *
 */
public class SpawnPoint extends Square {

    private ArrayList<WeaponCard> weaponCards;

    public SpawnPoint() {
        weaponCards = new ArrayList<WeaponCard>();
    }

    public List<WeaponCard> getWeaponCards() {
        return (ArrayList<WeaponCard>) weaponCards.clone();
    }

    public void addWeaponCard(WeaponCard weaponCard){
        weaponCards.add(weaponCard);
    }
}