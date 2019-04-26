package Model.Map;

import Model.Cards.CollectableInterface;
import Model.Cards.WeaponCard;
import Constants.Color;
import Model.Player.Character;

import java.util.*;

/**
 *
 */
public class SpawnPoint extends SquareAbstract {

    private ArrayList<WeaponCard> weaponCards;
    private ArrayList<Character> charactersList;
    private Room room;
    private Color color;
    private int xValue;
    private int yValue;
    // the following are just for visible squares
    private SquareAbstract nSquare;
    private SquareAbstract wSquare;
    private SquareAbstract eSquare;
    private SquareAbstract sSquare;

    protected SpawnPoint(int x, int y, Color color) {
        super(x,y,color);

        weaponCards = new ArrayList<>();
    }


    public List<WeaponCard> getWeaponCards() {
        return (ArrayList<WeaponCard>) weaponCards.clone();
    }

    public void addItem(CollectableInterface itemToAdd){
        weaponCards.add((WeaponCard)itemToAdd);
    }

    public void removeItem(CollectableInterface itemToRemove){
        weaponCards.remove((WeaponCard)itemToRemove);
    }

    @Override
    public String toString() {
        String stringToReturn = "Spawnpoint, Room: " + this.color + " Weapons: ";
        for(WeaponCard card : this.weaponCards){
            stringToReturn += card + " - ";
        }
        return stringToReturn;
    }
}