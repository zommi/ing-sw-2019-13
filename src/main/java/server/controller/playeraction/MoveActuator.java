package server.controller.playeraction;

import constants.Directions;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MoveActuator {

    public MoveActuator(){}

    public void actuate(PlayerAbstract player, List<Directions> moves){
        for(Directions dir : moves){
            player.move(dir);
        }
    }
}
