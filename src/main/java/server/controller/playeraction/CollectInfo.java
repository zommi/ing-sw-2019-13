package server.controller.playeraction;

import constants.Constants;

import java.io.Serializable;

public class CollectInfo {

    private int choice;

    public CollectInfo(){
        this.choice = Constants.NO_CHOICE;
    }

    public CollectInfo(int choice){
        this.choice = choice;
    }

    public int getChoice() {
        return choice;
    }
}
