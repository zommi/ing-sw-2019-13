package server.controller.playeraction.normalaction;

import constants.*;
import server.controller.playeraction.Action;
import server.controller.playeraction.MoveActuator;
import client.MoveInfo;
import server.controller.playeraction.MoveValidator;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MoveAction implements Action {
    private List<Direction> moves;
    private PlayerAbstract player;

    private MoveValidator validator;
    private MoveActuator actuator;

    public MoveAction(MoveInfo info, PlayerAbstract player){
        //(TODO change implementation!
        //this.moves = info.getMoves();
        this.player = player;
        this.validator = new MoveValidator();
        this.actuator = new MoveActuator();
    }


    public boolean execute(){
        return this.move();
    }

    private boolean move() {
        if(validator.validate(player,moves)){
            actuator.actuate(player,moves);
            return true;
        }else {
            return false;
        }
    }
}