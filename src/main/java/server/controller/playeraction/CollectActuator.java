package server.controller.playeraction;

import constants.Constants;
import constants.Direction;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.util.List;

public class CollectActuator {

    public CollectActuator(){}

    public void actuate(PlayerAbstract player, SquareAbstract square, int choice){
        player.setPosition(square);
        if(choice == Constants.NO_CHOICE){
            Square squarePlayer =  (Square)player.getPosition();
            player.collect(squarePlayer);
        }else {
            SpawnPoint spawnPoint = (SpawnPoint) player.getPosition();
            player.collect(spawnPoint, choice);
        }
    }
}
