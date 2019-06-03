package client;

import constants.Constants;

import java.io.Serializable;

public class CollectInfo implements Serializable, Info {

    /**
     * Int containing the index of the weapon the player
     * wants to collect from the spawn point. If the player
     * wants to collect an ammo tile it is set to NO CHOICE
     */
    private int row;
    private int col;
    private int choice;

    /**
     * constructor used if the player doesn't specify a choice. It is used
     * when the player wants to collect an ammo tile
     */
    public CollectInfo(int row, int col, int choice){
        this.choice = choice;
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    public int getChoice() {
        return choice;
    }
}
