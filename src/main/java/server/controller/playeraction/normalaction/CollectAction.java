package server.controller.playeraction.normalaction;

import client.CollectInfo;
import client.MoveInfo;
import constants.*;
import server.controller.Controller;
import server.controller.playeraction.*;
import server.model.map.GameMap;
import server.model.player.PlayerAbstract;

import java.util.List;

public class CollectAction implements Action {
    /**
     * list of directions the players specified in the MoveInfo packet
     * (it can be empty if the player doesn't want to move)
     */
    private List<Direction> moves;

    private Controller controller;

    /**
     * reference to the player that is doing the action
     */
    private PlayerAbstract player;

    /**
     * validator of the action
     */
    private CollectValidator validator;

    /**
     * actuator of the action
     */
    private CollectActuator actuator;

    /**
     * index of the weapon collected, it is set to NO_CHOICE if the player wants
     * to collect an ammo tile
     */
    private int weaponChoice;
    private MoveInfo moveInfo;
    private GameMap map;

    public CollectAction(MoveInfo moveInfo, CollectInfo collectInfo, PlayerAbstract player, GameMap currentMap){
        this.moveInfo = moveInfo;
        this.map = currentMap;
        this.player = player;
        this.weaponChoice = collectInfo.getChoice();
        this.validator = new CollectValidator();
        this.actuator = new CollectActuator();
    }


    @Override
    public boolean execute(Controller controller) {
        this.controller = controller;
        return this.collect();
    }

    public boolean collect(){
        if(validator.validate(player, map.getSquare(moveInfo.getCoordinateX(), moveInfo.getCoordinateY()),weaponChoice)) {
            actuator.actuate(player, map.getSquare(moveInfo.getCoordinateX(), moveInfo.getCoordinateY()) ,weaponChoice, controller);
            return true;
        }else {
            System.out.println("You can't collect");
            return false;
        }
    }
}
