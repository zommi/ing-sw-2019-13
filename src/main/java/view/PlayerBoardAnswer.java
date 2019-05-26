package view;

import server.model.player.PlayerBoard;


public class PlayerBoardAnswer implements ServerAnswer{
    private PlayerBoard result;

    public PlayerBoardAnswer(PlayerBoard p){
        this.result = p.createCopy(p);
    }

    public PlayerBoard getResult(){
        return this.result;
    }

    public int getRedAmmo(){
        //TODO
        return 0;
    }
    public int getBlueAmmo(){
        //TODO
        return 0;
    }
    public int getYellowAmmo(){
        //TODO
        return 0;
    }
}
