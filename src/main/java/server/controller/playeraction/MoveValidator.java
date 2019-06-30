package server.controller.playeraction;

import constants.Direction;
import server.model.map.GameMap;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;

import java.util.List;

public class MoveValidator {

    public MoveValidator(){}

    public boolean validate(PlayerAbstract player, SquareAbstract square){
        if(square == null)
            return false;

        return (square.distance(player.getPosition()) < 4 ||
                (square.distance(player.getPosition()) < 5 && player.getPlayerState() == PlayerState.BEFORE_FIRST_PLAYER_FF))
                && player.getPlayerState() != PlayerState.AFTER_FIRST_PLAYER_FF;
    }
}
