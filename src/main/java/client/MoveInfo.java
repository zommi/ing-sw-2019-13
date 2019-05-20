package client;

import constants.Direction;
import server.controller.Info;
import server.model.player.PlayerAbstract;

import java.io.Serializable;
import java.util.List;

public class MoveInfo implements Serializable, Info {

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
