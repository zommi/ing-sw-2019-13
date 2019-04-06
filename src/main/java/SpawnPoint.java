
import java.util.*;

/**
 *
 */
public class SpawnPoint implements SquareInterface {

    private ArrayList<WeaponCard> weaponCards;
    private ArrayList<Character> charactersList;
    private Room room;
    private Color color
    private int xValue;
    private int yValue;
    // the following are just for visible squares
    private SquareInterface nSquare;
    private SquareInterface wSquare;
    private SquareInterface eSquare;
    private SquareInterface sSquare;

    public SpawnPoint(int x, int y, char color) {
        this.xValue = x;
        this.yValue = y;
        //this.color = color;

        //new character list
        this.charactersList= new ArrayList<Character>();

        weaponCards = new ArrayList<WeaponCard>();
    }


    public List<WeaponCard> getWeaponCards() {
        return (ArrayList<WeaponCard>) weaponCards.clone();
    }

    public void addWeaponCard(WeaponCard weaponCard){
        weaponCards.add(weaponCard);
    }
}