package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;

public class MachineGun implements WeaponRulesInterface{

    @Override
    public boolean validate(ShootInfo pack){
        int effectsnumber = pack.getExtra().size();

        if(effectsnumber == 0)
            return false;

        if (effectsnumber > 3)
            return false;

        if (((effectsnumber == 2)||(effectsnumber == 1)||(effectsnumber == 3)) && (pack.getExtra().get(0) != 0)) //no extra without base
            return false;
        if ((effectsnumber == 2)&& (pack.getExtra().get(1) == 0)) //no duplicated extra
            return false;
        if ((effectsnumber == 3)&& ((pack.getExtra().get(1) != 1)||(pack.getExtra().get(2) != 2))) //no duplicated extra
            return false;

        for(int i = 0; i < effectsnumber; i++) {
            if(pack.getExtra().get(i).equals(0)){ //only basic effect
                if(pack.getTargets().get(0).size() > 2)
                    return false;
                if(pack.getTargets().get(0).size() == 2) {
                    if (!(pack.getPlayer().getPosition().getVisibleCharacters().contains(pack.getTargets().get(0).get(0))) ||
                            !(pack.getPlayer().getPosition().getVisibleCharacters().contains(pack.getTargets().get(0).get(1))))
                        return false;
                }
                if(pack.getTargets().get(0).size() == 1) {
                    if (!(pack.getPlayer().getPosition().getVisibleCharacters().contains(pack.getTargets().get(0).get(0))))
                        return false;
                }
            }
            else if(pack.getExtra().get(i).equals(1)){ //extra number 1
                if(pack.getTargets().get(i).size() > 1)
                    return false;
                if(pack.getTargets().get(0).size() == 1) {
                    if(!(pack.getTargets().get(0).get(0).equals(pack.getTargets().get(i).get(0))))
                        return false;
                }
                if(pack.getTargets().get(0).size() == 2) {
                    if(!(pack.getTargets().get(0).get(0).equals(pack.getTargets().get(i).get(0))) && !(pack.getTargets().get(0).get(1).equals(pack.getTargets().get(i).get(0))))
                        return false;
                }
            }
            else if(pack.getExtra().get(i).equals(2)){ //extra number 2
                if(pack.getTargets().get(i).size() != 2)
                    return false;
                if(pack.getTargets().get(0).size() == 1) {
                    //the target will be the same
                    if(!pack.getTargets().get(i).get(0).equals(pack.getTargets().get(0).get(0)))
                        return false;
                }
                if(pack.getTargets().get(0).size() == 2) {
                    if(!pack.getTargets().get(i).get(0).equals(pack.getTargets().get(0).get(0)) && !pack.getTargets().get(i).get(0).equals(pack.getTargets().get(0).get(1)))
                        return false;
                    if(effectsnumber == 3) {
                        if(pack.getTargets().get(i).get(0).equals(pack.getTargets().get(1).get(0)))
                            return false;
                    }
                }
                if(!(pack.getPlayer().getPosition().getVisibleCharacters().contains(pack.getTargets().get(i).get(1))))
                    return false; //it means that i can't see him
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
