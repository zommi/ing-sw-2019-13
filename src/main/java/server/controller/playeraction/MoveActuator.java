package server.controller.playeraction;

import constants.Direction;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MoveActuator {

    public MoveActuator(){}

    public void actuate(PlayerAbstract player, SquareAbstract square){
        player.setPosition(square);
    }
}
