package server.controller.playeraction.WeaponRules;

import constants.Constants;
import server.controller.playeraction.ShootInfo;
import server.model.items.AmmoCube;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;

import java.util.ArrayList;
import java.util.List;

public class VortexCannon implements WeaponRulesInterface{

    private static final int TARGET_INDEX = 0;

    private SquareAbstract playerCurrentPosition;
    private SquareAbstract vortexSquare;
    private GameCharacter targetOfBaseEffect;
    private List<GameCharacter> targetsOfFirstEffect;
    private List<Integer> effectsInOrder;
    private PlayerAbstract currentPlayer;
    private ArrayList<AmmoCube> costOfExtra;

    @Override
    public boolean validate(ShootInfo pack) {
        unpack(pack);

        if(!playerCurrentPosition.getVisibleSquares().contains(vortexSquare)){
            return false;
        }

        if(!targetOfBaseEffect.getPosition().getAdjacentSquares().contains(vortexSquare)
         || targetOfBaseEffect.getPosition().equals(vortexSquare)){
            return false;
        }

        if(effectsInOrder.size() == 2
            && currentPlayer.canPay(costOfExtra)
            && targetsOfFirstEffect.size() <= 2){
            for(GameCharacter target : targetsOfFirstEffect){
                if(target.getPosition().getAdjacentSquares().contains(vortexSquare)
                || target.getPosition().equals(vortexSquare)){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void actuate(ShootInfo pack) {

    }

    public void unpack(ShootInfo pack) {
        this.currentPlayer = pack.getPlayer();
        this.playerCurrentPosition = pack.getPlayer().getPosition();
        this.vortexSquare = pack.getChosenSquare();
        this.targetOfBaseEffect = pack.getTargets().get(Constants.TARGETS_OF_EFFECT_ZERO).get(TARGET_INDEX);
        this.targetsOfFirstEffect = pack.getTargets().get(Constants.TARGETS_OF_EFFECT_ONE);
        this.effectsInOrder = pack.getExtra();
        this.costOfExtra = pack.getWeapon().getCost1();
    }

}
