package server.controller.playeraction.normalaction;

import server.controller.playeraction.Action;
import client.ShootInfo;
import server.controller.playeraction.WeaponRulesInterface;

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
