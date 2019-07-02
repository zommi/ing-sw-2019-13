package server.controller.playeraction.normalaction;

import constants.*;
import server.controller.Controller;
import server.controller.playeraction.Action;
import server.controller.playeraction.MoveActuator;
import client.MoveInfo;
import server.controller.playeraction.MoveValidator;
import server.model.map.GameMap;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MoveAction implements Action {
    private SquareAbstract destination;
    private PlayerAbstract player;

    private MoveValidator validator;
    private MoveActuator actuator;

    public MoveAction(MoveInfo info, PlayerAbstract player, GameMap map){
        this.destination = map.getSquare(info.getRow(),info.getCol());
        this.player = player;
        this.validator = new MoveValidator();
        this.actuator = new MoveActuator();
    }


    public boolean execute(Controller controller){
        return this.move(controller);
    }

    private boolean move(Controller controller) {
        if(validator.validate(player,destination)){
            actuator.actuate(player,destination);
            return true;
        }else {
            return false;
        }
    }
}