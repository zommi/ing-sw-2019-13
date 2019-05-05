package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;

public class Thor implements WeaponRulesInterface{
    @Override
    public boolean validate(ShootInfo pack){
        int effectsnumber = pack.getExtra().size();

        if(effectsnumber == 0)
            return false;
        if(effectsnumber > 3)
            return false;
        if(effectsnumber == 3){
            if((pack.getExtra().get(0) != 0)||(pack.getExtra().get(1) != 1 )|| (pack.getExtra().get(2) != 2))
                return false;
        }
        if(effectsnumber == 2){
            if((pack.getExtra().get(0) != 0)||(pack.getExtra().get(1) != 1 ))
                return false;
        }
        if(effectsnumber == 1){
            if(pack.getExtra().get(0) != 0)
                return false;
        }
        for(int i = 0; i < effectsnumber; i++) {
            if(pack.getExtra().get(i).equals(0)){ //extra number 0, basic effect
                if(pack.getTargets().get(i).size() != 1)
                    return false; //only one targetfor the basic effect
                if(!pack.getPlayer().getPosition().getVisibleCharacters().contains(pack.getTargets().get(i).get(0)))
                    return false;
            }
            if(pack.getExtra().get(i).equals(1)){
                if(pack.getTargets().get(i).size() != 1)
                    return false;
                if(!pack.getTargets().get(0).get(0).getPosition().getVisibleCharacters().contains(pack.getTargets().get(i).get(0)))
                    return false;
            }
            if(pack.getExtra().get(i).equals(2)){
                if(pack.getTargets().get(i).size() != 1)
                    return false;
                if(!pack.getTargets().get(1).get(0).getPosition().getVisibleCharacters().contains(pack.getTargets().get(i).get(0)))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void actuate(ShootInfo pack){

    }

    @Override
    public void unpack(ShootInfo pack){

    }
}
