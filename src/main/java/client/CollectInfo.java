package client;

import constants.Constants;
import server.controller.Info;

import java.io.Serializable;

public class CollectInfo implements Serializable, Info {

    /**
     * Int containing the index of the weapon the player
     * wants to collect from the spawn point. If the player
     * wants to collect an ammo tile it is set to NO CHOICE
     */
    private int choice;

    /**
     * constructor used if the player doesn't specify a choice. It is used
     * when the player wants to collect an ammo tile
     */
    public CollectInfo(){
        this.choice = Constants.NO_CHOICE;
    }

    /**
     * constructor used if the player specifies a choice,
     * it is used when the the player wants to collect a weapon from a spawn point
     * @param choice
     */
    public CollectInfo(int choice){
        this.choice = choice;
    }

    public int getChoice() {
        return choice;
    }
}
