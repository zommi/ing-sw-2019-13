package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class GrenadeLauncher implements WeaponRulesInterface{
    private SquareAbstract playerPosition;
    private List<Integer> effectsInOrder;
    private GameCharacter targetsBaseEffect;
    private ArrayList<GameCharacter> targetsAddedEffect;
    private SquareAbstract squareAddedEffect;
    private boolean canPay;
    private SquareAbstract targetBaseEffectOriginalPosition;
    private SquareAbstract squareToMoveTargetInto;

    //TODO make tests
    @Override
    public boolean validate(ShootInfo pack) {
        unpack(pack);

        //check effects list TODO
        //if((effectsInOrder.size() == 1 && effectsInOrder.get(0) != 0) || (effectsInOrder.size() == 2 && ))

        if(!playerPosition.getVisibleSquares().contains(targetBaseEffectOriginalPosition)
            || (!targetBaseEffectOriginalPosition.getAdjacentSquares().contains(squareToMoveTargetInto)
                && squareToMoveTargetInto != null)){
            return false;
        }

        if(!playerPosition.getVisibleSquares().contains(squareAddedEffect)){
            return false;
        }

        for(GameCharacter character : targetsAddedEffect){
            if(!character.getPosition().equals(squareAddedEffect)){
                return false;
            }
        }

        return canPay;
    }

    @Override
    public void actuate(ShootInfo pack) {

    }

    @Override
    public void unpack(ShootInfo pack) {
        this.playerPosition = pack.getPlayer().getPosition();
        this.effectsInOrder = pack.getExtra();
        this.targetsBaseEffect = pack.getTargets().get(0).get(0);
        this.targetBaseEffectOriginalPosition = pack.getTargets().get(0).get(0).getPosition();
        this.squareToMoveTargetInto = pack.getTargetSquares().get(0);
        this.targetsAddedEffect = pack.getTargets().get(1);
        this.squareAddedEffect = pack.getChosenSquare();
        this.canPay = pack.getExtra().size() != 2 || pack.getPlayer().canPay(pack.getWeapon().getCost1());
    }
}
