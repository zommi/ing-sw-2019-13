package server.controller.playeraction.normalaction;

import client.weapons.ShootPack;
import server.controller.playeraction.Action;
import server.controller.playeraction.WeaponRulesInterface;

public class ShootAction implements Action {

    private ShootPack shootPack;

    private WeaponRulesInterface weaponRule;

    public ShootAction(ShootPack shootPack){
        this.shootPack = shootPack;
    }

    @Override
    public boolean execute() {
        return true;//this.shoot();
    }



}
