package server.model.map;

import constants.Color;
import server.controller.Controller;
import server.model.cards.AmmoTile;
import server.model.cards.CollectableInterface;
import server.model.gameboard.AmmoTileDeck;

import java.io.Serializable;

public class Square extends SquareAbstract implements Serializable {

    private AmmoTile ammoTile;

    public Square(int x, int y, Color color, GameMap gameMap) {
        super(x, y, color, gameMap);
    }

    public AmmoTile getAmmoTile() {
        return ammoTile;
    }

    public void addItem(CollectableInterface itemToAdd) {
        this.ammoTile = (AmmoTile)itemToAdd;
    }

    public void removeItem(CollectableInterface itemToRemove, Controller controller){
        ammoTile = null;
        controller.addSquareToUpdate(this);
        //addItem(controller.drawAmmo());
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