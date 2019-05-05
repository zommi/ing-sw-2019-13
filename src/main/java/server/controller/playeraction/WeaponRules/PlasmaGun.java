package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;

public class PlasmaGun implements WeaponRulesInterface{

    @Override
    public boolean validate(ShootInfo pack){
        int effectsnumber = pack.getExtra().size();

        if(effectsnumber == 0)
            return false;
        if(effectsnumber > 3)
            return false;

        if((effectsnumber == 3)||(effectsnumber == 2))
        {
            if(pack.getExtra().get(0) == 2)
                return false;
            if(pack.getExtra().get(1) == 2 && (pack.getExtra().get(0) == 1))
                return false;
        }
        if(effectsnumber == 1){
            if(pack.getExtra().get(0) == 2)
                return false;
        }


        for(int i = 0; i < effectsnumber; i++) {
            if(pack.getExtra().get(i).equals(0)){
                if(pack.getTargets().get(i).size() != 1)
                    return false; //only one target
                if(!pack.getPlayer().getPosition().getVisibleCharacters().contains(pack.getTargets().get(i).get(0)))
                    return false;
            }

            if(pack.getExtra().get(i).equals(1)){
                if(!pack.getPlayer().getPosition().getExactlyOneMovementCharacters().contains(pack.getYourSquares().get(0)) &&
                        (!pack.getPlayer().getPosition().getExactlyTwoMovementsSquares().contains(pack.getYourSquares().get(0))))
                    return false;
            }

            if(pack.getExtra().get(i).equals(2)){
                if(pack.getTargets().get(1).size() != 1) //it will be the number 1 for sure: the second extra does not prepare any list of targets
                    return false; //only one target
                if(!pack.getTargets().get(0).get(0).equals(pack.getTargets().get(1).get(0)))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void actuate(ShootInfo pack){

    }

    @Override
    public void unpack(ShootInfo pack) {

    }
}
