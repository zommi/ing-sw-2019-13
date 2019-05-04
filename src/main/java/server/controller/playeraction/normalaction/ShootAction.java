package server.controller.playeraction.normalaction;

import server.controller.playeraction.Action;
import server.controller.playeraction.MoveInfo;
import server.controller.playeraction.ShootInfo;
import server.controller.playeraction.WeaponRules.WeaponRulesInterface;
import server.model.cards.*;
import constants.Color;
import constants.Directions;
import server.model.player.PlayerAbstract;

import java.util.List;

public class ShootAction implements Action {

    private ShootInfo shootInfo;

    private WeaponRulesInterface weaponRule;

    public ShootAction(ShootInfo shootInfo, WeaponRulesInterface weaponRule){
        this.shootInfo = shootInfo;
        this.weaponRule = weaponRule;
    }

    @Override
    public boolean execute() {
        return this.shoot();
    }

    public boolean shoot(){
        if(this.weaponRule.validate(shootInfo)){
            this.weaponRule.actuate(shootInfo);
            return true;
        } else {
            return false;
        }
    }
}
