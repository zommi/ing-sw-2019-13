package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;

public class Elecroscythe implements WeaponRulesInterface{

    @Override
    public boolean validate(ShootInfo pack){

        int effectsnumber = pack.getExtra().size();

        if(effectsnumber == 0)
            return false;

        if (effectsnumber > 1)
            return false;

        if(effectsnumber == 1){
            if((pack.getExtra().get(0) != 0) && (pack.getExtra().get(0) != 1))
                return false;
        }
        return true; //then all the targets are visible
    }


    @Override
    public void actuate(ShootInfo pack){

    }
    @Override
    public void unpack(ShootInfo pack){

    }
}
