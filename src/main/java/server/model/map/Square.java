package server.model.map;

import constants.Color;
import server.model.cards.AmmoTile;
import server.model.cards.CollectableInterface;

public class Square extends SquareAbstract {

    private AmmoTile ammoTile;                          //TODO optional?

    public Square(int x, int y, Color color, GameMap gameMap) {
        super(x, y, color, gameMap);
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
    public boolean isEmpty() {
        return ammoTile == null;
    }
}