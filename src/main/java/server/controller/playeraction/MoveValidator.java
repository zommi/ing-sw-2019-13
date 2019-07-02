package server.controller.playeraction;

import server.model.game.GameState;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;

public class MoveValidator {

    public boolean validate(PlayerAbstract player, SquareAbstract square){
        if(square == null)
            return false;

        return (square.distance(player.getPosition()) < 4 && player.getCurrentGame().getCurrentState() == GameState.NORMAL) ||
                (square.distance(player.getPosition()) < 5 && player.getCurrentGame().getCurrentState() == GameState.FINAL_FRENZY &&
                        player.getPlayerState() == PlayerState.BEFORE_FIRST_PLAYER_FF);
    }
}
