package server.controller.playeraction.WeaponRules;

import constants.Constants;
import server.controller.playeraction.ShootInfo;
import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class Furnace implements WeaponRulesInterface{

    private SquareAbstract playerPosition;
    private Room targetRoom;
    private SquareAbstract targetSquare;
    private boolean basicModeFlag;
    private List<GameCharacter> targets;


    @Override
    public boolean validate(ShootInfo pack) {
        unpack(pack);

        if(basicModeFlag){
            for(GameCharacter character : targets){
                if(!character.getPosition().getRoom().equals(targetRoom)) return false;
            }
            return playerPosition.getVisibleRooms().contains(targetRoom);
        }else{
            for(GameCharacter character : targets){
                if(!character.getPosition().equals(targetSquare)) return false;
            }
            return playerPosition.getAdjacentSquares().contains(targetSquare);
        }
    }

    @Override
    public void actuate(ShootInfo pack) {

    }

    public void unpack(ShootInfo pack){
        this.playerPosition = pack.getPlayer().getPosition();
        this.targetRoom = pack.getChosenRoom();
        this.targetSquare = pack.getChosenSquare();
        this.basicModeFlag = pack.getExtra().get(0) == 0;
        this.targets = pack.getTargets().get(Constants.TARGETS_OF_EFFECT_ZERO);
    }
}
