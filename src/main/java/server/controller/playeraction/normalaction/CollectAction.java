package server.controller.playeraction.normalaction;

import client.CollectInfo;
import client.MoveInfo;
import server.controller.Controller;
import server.controller.playeraction.*;
import server.model.map.GameMap;
import server.model.player.PlayerAbstract;

public class CollectAction implements Action {

    /**
     * the controller of the game in which this action is made
     */
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
    private CollectInfo collectInfo;

    public CollectAction(MoveInfo moveInfo, CollectInfo collectInfo, PlayerAbstract player, GameMap currentMap){
        this.moveInfo = moveInfo;
        this.map = currentMap;
        this.player = player;
        this.weaponChoice = collectInfo.getChoice();
        this.validator = new CollectValidator();
        this.actuator = new CollectActuator();
        this.collectInfo = collectInfo;
    }


    @Override
    public boolean execute(Controller controller) {
        this.controller = controller;
        return this.collect();
    }

    public boolean collect(){
        if(validator.validate(player, map.getSquare(moveInfo.getRow(),moveInfo.getCol()),weaponChoice, collectInfo)) {
            actuator.actuate(player, map.getSquare(moveInfo.getRow(), moveInfo.getCol()) ,weaponChoice, controller, collectInfo);
            return true;
        }else {
            System.out.println("You can't collect");
            return false;
        }
    }
}
