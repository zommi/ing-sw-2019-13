package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class RocketLauncher implements WeaponRulesInterface{
    private SquareAbstract playerPosition;
    private List<Integer> effectsInOrder;
    private GameCharacter targetBaseEffect;
    private SquareAbstract targetBaseEffectOriginalPosition;
    private SquareAbstract squareToMovePlayerInto;
    private SquareAbstract squareToMoveTargetInto;
    private ArrayList<GameCharacter> targetsAddedEffect;
    private boolean canPayEffect1;
    private boolean canPayEffect2;

    @Override
    public boolean validate(ShootInfo pack) {
        //TODO check effects numbers

        if(!playerPosition.getVisibleSquares().contains(targetBaseEffectOriginalPosition)
                || (squareToMoveTargetInto != null
                && targetBaseEffectOriginalPosition.getAdjacentSquares().contains(squareToMoveTargetInto))){
            return false;
        }

        if(squareToMovePlayerInto != null &&
                (playerPosition.getExactlyTwoMovementsSquares().contains(squareToMovePlayerInto)
                || playerPosition.getAdjacentSquares().contains(squareToMovePlayerInto))){
            return false;
        }

        for(GameCharacter character : targetsAddedEffect){
            if(!character.getPosition().equals(targetBaseEffectOriginalPosition)){
                return false;
            }
        }

        //TODO check if player can pay bonus effects
        return true;
    }

    @Override
    public void actuate(ShootInfo pack) {

    }

    @Override
    public void unpack(ShootInfo pack) {
        this.playerPosition = pack.getPlayer().getPosition();
        this.effectsInOrder = pack.getExtra();
        this.targetBaseEffect = pack.getTargets().get(0).get(0);
        this.targetBaseEffectOriginalPosition = pack.getTargets().get(0).get(0).getPosition();
        this.squareToMovePlayerInto = pack.getYourSquares().get(0);
        this.squareToMoveTargetInto = pack.getTargetSquares().get(0);
        this.targetsAddedEffect = pack.getTargets().get(1);
        this.canPayEffect1 = pack.getPlayer().canPay(pack.getWeapon().getCost1());
        this.canPayEffect2 = pack.getPlayer().canPay(pack.getWeapon().getCost2());
    }
}
