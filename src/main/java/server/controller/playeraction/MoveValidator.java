package server.controller.playeraction;

import constants.Direction;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MoveValidator {

    public MoveValidator(){}

    public boolean validate(PlayerAbstract player, List<Direction> moves){
        if(moves.size() > 3) return false;

        SquareAbstract squareTemp = player.getPosition();
        for(Direction dir : moves){
            if(squareTemp.getNearFromDir(dir) == null)return false;
            squareTemp.getNearFromDir(dir);
        }
        return true;
    }
}
