package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;

import java.util.ArrayList;

public class LockRifle implements WeaponRulesInterface{

    @Override
    public boolean validate(ShootInfo pack) {

        int effectsnumber = pack.getExtra().size();

        if(effectsnumber == 0)
            return false;

        if (effectsnumber > 2)
            return false;

        if ((effectsnumber == 2) && ((pack.getExtra().get(0) != 0) || (pack.getExtra().get(1) != 1)))
            return false;

        if ((effectsnumber == 1) && (pack.getExtra().get(0) != 0))
            return false;


        for(int i = 0; i < effectsnumber; i++) {
            if(pack.getExtra().get(i).equals(0)){ //only basic effect
                if((pack.getTargets().get(0).size() > 1) || !(pack.getPlayer().getPosition().getVisibleCharacters().contains(pack.getTargets().get(0).get(0))))
                    return false;
            }
            else if(pack.getExtra().get(i).equals(1)){ //extra number 1
                if((pack.getTargets().get(1).size() > 1) || !(pack.getPlayer().getPosition().getVisibleCharacters().contains(pack.getTargets().get(1).get(0))))
                    return false;
            }
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
