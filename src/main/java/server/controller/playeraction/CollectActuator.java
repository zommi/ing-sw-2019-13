package server.controller.playeraction;

import constants.Constants;
import constants.Direction;
import server.controller.Controller;
import server.model.cards.CardInterface;
import server.model.cards.CollectableInterface;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.util.List;

public class CollectActuator {

    public CollectActuator(){}

    public void actuate(PlayerAbstract player, SquareAbstract square, int choice, Controller controller){
        player.setPosition(square);
        CollectableInterface card;
        if(choice == Constants.NO_CHOICE){
            Square squarePlayer =  (Square)player.getPosition();
            player.collect(squarePlayer);
            card = ((Square) player.getPosition()).getAmmoTile();
            squarePlayer.removeItem(card, controller);
        }else {
            SpawnPoint spawnPoint = (SpawnPoint) player.getPosition();
            player.collect(spawnPoint, choice);
            card = ((SpawnPoint)player.getPosition()).getWeaponCards().get(choice);
            spawnPoint.removeItem(card, controller);
        }
    }
}
