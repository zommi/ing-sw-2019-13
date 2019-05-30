package server.controller.playeraction;

import constants.Direction;
import server.model.map.GameMap;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MoveValidator {

    public MoveValidator(){}

    public boolean validate(PlayerAbstract player, SquareAbstract square){
        return square.distance(player.getPosition()) < 4;
    }
}
