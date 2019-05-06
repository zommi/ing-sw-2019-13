package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;

public class Elecroscythe implements WeaponRulesInterface{

    private int effectsnumber;
    private int extraPosition0;

    @Override
    public boolean validate(ShootInfo pack){

        if(effectsnumber == 0)
            return false;

        if (effectsnumber > 1)
            return false;

        if((effectsnumber == 1)&&((extraPosition0 != 0) && (extraPosition0 != 1)))
            return false;
        return true; //then all the targets are visible
    }


    @Override
    public void actuate(ShootInfo pack){

    }
    @Override
    public void unpack(ShootInfo pack){
        this.effectsnumber = pack.getExtra().size();
        this.extraPosition0 = pack.getExtra().get(0);
    }
}
