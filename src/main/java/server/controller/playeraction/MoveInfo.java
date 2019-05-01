package server.controller.playeraction;

import constants.Directions;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MoveInfo {

    private List<Directions> moves;

    private PlayerAbstract player;

    public MoveInfo(PlayerAbstract player, List<Directions> moves){
        this.player = player;
        this.moves = moves;
    }

    public PlayerAbstract getPlayer() {
        return player;
    }

    public List<Directions> getMoves() {
        return moves;
    }
}
