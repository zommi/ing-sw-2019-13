package server.controller.playeraction.normalaction;

import constants.*;
import server.controller.playeraction.MoveInfo;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MoveAction {
    private List<Directions> moves;
    private PlayerAbstract player;

    public MoveAction(MoveInfo info){
        this.moves = info.getMoves();
        this.player = info.getPlayer();
    }


    public void execute(){
        this.move();
    }

    private void move() {
        for(Directions dir : moves){
            player.move(dir);
        }
    }
}
