package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;

public class TractorBeam implements WeaponRulesInterface{

    public boolean validate(ShootInfo pack){
        return true;
    }

    public void actuate(ShootInfo pack){

    }
}
