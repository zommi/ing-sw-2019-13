package server.model.map;

import server.model.cards.AmmoTile;
import server.model.cards.CollectableInterface;
import constants.Color;
import server.model.player.Character;

import java.util.*;

public class Square extends SquareAbstract {

    private AmmoTile ammoTile;                          //TODO optional?
    private ArrayList<Character> charactersList;
    private Room room;
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
        return "Square, Room: " + getColor() + ", AmmoTile: \n" + this.ammoTile;
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }
}