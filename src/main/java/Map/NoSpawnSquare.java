package Map;

import Cards.AmmoTile;
import Constants.Color;
import Player.Character;

import java.util.*;

public class NoSpawnSquare extends Square {

    private AmmoTile ammoTile;                          //TODO optional?
    private ArrayList<Character> charactersList;
    private Room room;
    private Color color;
    private int xValue;
    private int yValue;
    // the following are just for visible squares
    private Square nSquare;
    private Square wSquare;
    private Square eSquare;
    private Square sSquare;

    public NoSpawnSquare(int x, int y, char color) {
        super(x,y,color);



    }

    public AmmoTile getAmmoTile() {
        return ammoTile;
    }

    public void setAmmoTile(AmmoTile ammoTile) {
        this.ammoTile = ammoTile;
    }

    public void removeAmmoTile(){
        ammoTile = null;
    }
}