package server.controller.playeraction.WeaponRules;

import constants.Directions;
import server.controller.playeraction.ShootInfo;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class FlameThrower implements WeaponRulesInterface{
    private boolean basicModeFlag;
    private SquareAbstract playerPosition;
    private ArrayList<GameCharacter> targetsInFirstSquare;
    private ArrayList<GameCharacter> targetsInSecondSquare;
    private Directions direction;
    private boolean canPay;

    @Override
    public boolean validate(ShootInfo pack) {
        unpack(pack);

        SquareAbstract firstSquare = targetsInFirstSquare.get(0).getPosition();
        if(!playerPosition.getAdjacentSquares().contains(firstSquare)){
            return false;
        }

        if(basicModeFlag){
            return !(targetsInSecondSquare != null
                    && !firstSquare.getNearFromDir(direction).equals(targetsInSecondSquare.get(0).getPosition()));
        }else{
            return !(checkPositions(targetsInFirstSquare,firstSquare)
                    && targetsInSecondSquare != null
                    && checkPositions(targetsInSecondSquare,firstSquare.getNearFromDir(direction))
                    && canPay);
        }
    }

    @Override
    public void actuate(ShootInfo pack) {

    }

    @Override
    public void unpack(ShootInfo pack) {
        this.basicModeFlag = pack.getExtra().get(0) == 0;
        this.playerPosition = pack.getPlayer().getPosition();
        this.targetsInFirstSquare = pack.getTargets().get(0);
        this.targetsInSecondSquare = pack.getTargets().get(1);
        this.canPay = pack.getPlayer().canPay(pack.getWeapon().getCost1());

        SquareAbstract firstSquare = targetsInFirstSquare.get(0).getPosition();

        for(Directions dir : Directions.values()){
            if(playerPosition.getTwoSquaresInTheSameDirection(dir).contains(firstSquare)){
                this.direction = dir;
                break;
            }
        }

    }

    private boolean checkPositions(List<GameCharacter> targets, SquareAbstract square){
        for(GameCharacter target : targets){
            if(!target.getPosition().equals(square)){
                return false;
            }
        }
        return true;
    }


}
