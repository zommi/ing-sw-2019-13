package view;

import server.model.player.PlayerBoard;


public class PlayerBoardAnswer implements ServerAnswer{
    private PlayerBoard result;

    public PlayerBoardAnswer(PlayerBoard p){
        this.result = p;
    }

    public PlayerBoard getResult(){
        return this.result;
    }

    public int getRedAmmo(){
        return result.getRedAmmo();
    }
    public int getBlueAmmo(){
        return result.getBlueAmmo();
    }
    public int getYellowAmmo(){
        return result.getYellowAmmo();
    }
    public String getCharacterName(){
        return result.getCharacterName();
    }
    public int getRow(){
        return result.getRow();
    }
    public int getCol(){
        return result.getCol();
    }
}
