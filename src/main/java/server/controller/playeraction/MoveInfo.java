package server.controller.playeraction;

import constants.Direction;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MoveInfo {

    private List<Direction> moves;

    private PlayerAbstract player;

    public MoveInfo(PlayerAbstract player, List<Direction> moves){
        this.player = player;
        this.moves = moves;
    }

    public PlayerAbstract getPlayer() {
        return player;
    }

    public List<Direction> getMoves() {
        return moves;
    }
}
