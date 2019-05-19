package server.controller.playeraction;

import constants.Direction;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MoveActuator {

    public MoveActuator(){}

    public void actuate(PlayerAbstract player, List<Direction> moves){
        for(Direction dir : moves){
            player.move(dir);
        }
    }
}
