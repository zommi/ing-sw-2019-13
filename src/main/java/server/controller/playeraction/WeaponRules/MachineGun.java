package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.player.GameCharacter;

import java.util.List;

public class MachineGun implements WeaponRulesInterface{

    private int effectsnumber;
    private int extraPosition0;
    private int extraPosition1;
    private int extraPosition2;
    private List<GameCharacter> target0;
    private List<GameCharacter> target1;
    private List<GameCharacter> playerPositionVisibleCharacters;

    @Override
    public boolean validate(ShootInfo pack){

        if((effectsnumber == 0) || (effectsnumber > 3))
            return false;
        if (((effectsnumber == 2)||(effectsnumber == 1)||(effectsnumber == 3)) && (extraPosition0  != 0)) //no extra without base
            return false;
        if ((effectsnumber == 2)&& (extraPosition1 == 0)) //no duplicated extra
            return false;
        if ((effectsnumber == 3)&& ((extraPosition1 != 1)||(extraPosition2 != 2))) //no duplicated extra
            return false;

        for(int i = 0; i < effectsnumber; i++) {
            if(pack.getExtra().get(i).equals(0)){ //only basic effect
                if(target0.size() > 2)
                    return false;
                if((target0.size() == 2)&&(!(playerPositionVisibleCharacters.contains(target0.get(0))) ||
                            !(playerPositionVisibleCharacters.contains(target0.get(1)))))
                        return false;
                if((target0.size() == 1) && (!(playerPositionVisibleCharacters.contains(target0.get(0)))))
                        return false;
                }
            else if(pack.getExtra().get(i).equals(1)){ //extra number 1
                if(pack.getTargets().get(i).size() > 1)
                    return false;
                if((target0.size() == 1)&& !(target0.get(0).equals(pack.getTargets().get(i).get(0))))
                        return false;
                if((target0.size() == 2)&& !(target0.get(0).equals(pack.getTargets().get(i).get(0))) && !(target0.get(1).equals(pack.getTargets().get(i).get(0))))
                        return false;
            }
            else if(pack.getExtra().get(i).equals(2)){ //extra number 2
                if(pack.getTargets().get(i).size() != 2)
                    return false;
                if((target0.size() == 1) && (!pack.getTargets().get(i).get(0).equals(target0.get(0))))//the target will be the same
                    return false;
                if(target0.size() == 2) {
                    if(!pack.getTargets().get(i).get(0).equals(target0.get(0)) && !pack.getTargets().get(i).get(0).equals(target0.get(1)))
                        return false;
                    if((effectsnumber == 3) && (pack.getTargets().get(i).get(0).equals(target1.get(0))))
                        return false;
                }
                if(!(playerPositionVisibleCharacters.contains(pack.getTargets().get(i).get(1))))
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
        this.effectsnumber = pack.getExtra().size();
        this.extraPosition0 = pack.getExtra().get(0);
        this.extraPosition1 = pack.getExtra().get(1);
        this.extraPosition2 = pack.getExtra().get(2);
        this.target0 = pack.getTargets().get(0);
        this.target1 = pack.getTargets().get(1);
        this.playerPositionVisibleCharacters = pack.getPlayer().getPosition().getVisibleCharacters();
    }

}
