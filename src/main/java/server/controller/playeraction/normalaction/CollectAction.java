package server.controller.playeraction.normalaction;

import constants.*;
import server.controller.playeraction.*;
import server.model.player.PlayerAbstract;

import java.util.List;

public class CollectAction implements Action {
    private List<Directions> moves;
    private PlayerAbstract player;
    private CollectValidator validator;
    private CollectActuator actuator;
    private int weaponChoice;

    public CollectAction(MoveInfo moveInfo, CollectInfo collectInfo){
        this.moves = moveInfo.getMoves();
        this.player = moveInfo.getPlayer();
        this.weaponChoice = collectInfo.getChoice();
        this.validator = new CollectValidator();
        this.actuator = new CollectActuator();
    }


    @Override
    public void execute() {
        this.collect();
    }

    public boolean collect(){
        if(validator.validate(player,moves,weaponChoice)) {
            actuator.actuate(player,moves,weaponChoice);
            return true;
        }else {
            return false;
        }
    }
}
