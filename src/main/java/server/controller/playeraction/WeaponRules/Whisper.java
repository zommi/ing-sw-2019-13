package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;

public class Whisper implements WeaponRulesInterface{

    public boolean validate(ShootInfo pack){
        int effectsnumber = pack.getExtra().size();

        if(effectsnumber == 0)
            return false;
        if(effectsnumber > 1)
            return false;
        if(pack.getExtra().get(0) != 0)
            return false;

        if(!pack.getPlayer().getPosition().getVisibleCharacters().contains(pack.getTargets().get(0).get(0))||
                !pack.getPlayer().getPosition().getAtLeastTwoMovementCharacters().contains(pack.getTargets().get(0).get(0)))
            return false;

        return true;
    }

    public void actuate(ShootInfo pack){

    }

    @Override
    public void unpack(ShootInfo pack) {

    }
}
