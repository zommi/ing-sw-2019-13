package server.controller.playeraction.normalaction;

import constants.*;
import server.controller.playeraction.Action;
import server.controller.playeraction.MoveInfo;
import server.model.player.PlayerAbstract;

import java.util.List;

public class CollectAction implements Action {
    private List<Directions> moves;
    private PlayerAbstract player;

    public CollectAction(MoveInfo info){
        this.moves = info.getMoves();
        this.player = info.getPlayer();
    }


    @Override
    public void execute() {
        this.collect();
    }

    public void collect(){
        for(Directions dir : moves){
            player.move(dir);
        }
        //player.getPosition().getItem());
    }
}
