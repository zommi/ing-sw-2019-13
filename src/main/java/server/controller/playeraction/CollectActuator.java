package server.controller.playeraction;

import constants.Constants;
import constants.Directions;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.util.List;

public class CollectActuator {

    public CollectActuator(){}

    public void actuate(PlayerAbstract playerPlaying, List<Directions> movements, int choice){
        for(Directions dir : movements){
            playerPlaying.move(dir);
        }
        if(choice == Constants.NO_CHOICE){
            Square square =  (Square)playerPlaying.getPosition();
            playerPlaying.collect(square);
        }else {
            SpawnPoint spawnPoint = (SpawnPoint) playerPlaying.getPosition();
            playerPlaying.collect(spawnPoint, choice);
        }
    }
}
