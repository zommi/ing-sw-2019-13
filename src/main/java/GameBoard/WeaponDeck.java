package GameBoard;

import java.util.*;


public class WeaponDeck {

    private int size;

    public WeaponDeck(int sizedeck) {
        this.size = sizedeck;
    }


    public int getSize() {
        return size;
    }

    public void draw()
    {
        this.size = this.size - 1;
    }

    public void shuffle() {
        // TODO implement here
        return null;
    }

}