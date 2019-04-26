package Model.Map;

import Model.Cards.AmmoTile;
import Model.Cards.CollectableInterface;
import Constants.Color;
import Model.Player.Character;

import java.util.*;

public class Square extends SquareAbstract {

    private AmmoTile ammoTile;                          //TODO optional?
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

    public Square(int x, int y, Color color) {
        super(x,y,color);
    }

    public AmmoTile getAmmoTile() {
        return ammoTile;
    }

    public void addItem(CollectableInterface itemToAdd) {
        this.ammoTile = (AmmoTile)itemToAdd;
    }

    public void removeItem(CollectableInterface itemToRemove){
        ammoTile = null;
    }

    @Override
    public String toString() {
        return "Square, Room: " + this.color + ", AmmoTile: \n" + this.ammoTile;
    }
}