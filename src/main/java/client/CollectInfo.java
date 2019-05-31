package client;

import constants.Constants;

import java.io.Serializable;

public class CollectInfo implements Serializable, Info {

    /**
     * Int containing the index of the weapon the player
     * wants to collect from the spawn point. If the player
     * wants to collect an ammo tile it is set to NO CHOICE
     */
    private int x;
    private int y;
    private int choice;

    /**
     * constructor used if the player doesn't specify a choice. It is used
     * when the player wants to collect an ammo tile
     */
    public CollectInfo(int x, int y, int choice){
        this.choice = Constants.NO_CHOICE;
        this.x = x;
        this.y = y;
    }

    public int getCoordinateX(){
        return this.x;
    }

    public int getCoordinateY(){
        return this.y;
    }

    public int getChoice() {
        return choice;
    }
}
