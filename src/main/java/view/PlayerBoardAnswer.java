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


}
