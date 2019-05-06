package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.player.GameCharacter;

import java.util.List;

public class Thor implements WeaponRulesInterface{

    private List<GameCharacter> playerPositionVisibleCharacters;
    private int effectsnumber;
    private int extraPosition0;
    private int extraPosition1;
    private int extraPosition2;
    private List<GameCharacter> target0;
    private List<GameCharacter> target1;
    private List<Integer> extra;

    @Override
    public boolean validate(ShootInfo pack){


        if((effectsnumber == 0)||(effectsnumber > 3))
            return false;

        if((effectsnumber == 3)&&((extraPosition0 != 0)||(extraPosition1 != 1 )||(extraPosition2 != 2)))
            return false;

        if((effectsnumber == 2)&&((extraPosition0 != 0)||(extraPosition1 != 1 )))
            return false;

        if((effectsnumber == 1)&&(extraPosition0 != 0))
            return false;

        for(int i = 0; i < effectsnumber; i++){
            if(extra.get(i).equals(0)){ //extra number 0, basic effect
                if(pack.getTargets().get(i).size() != 1)
                    return false; //only one targetfor the basic effect
                if(!playerPositionVisibleCharacters.contains(pack.getTargets().get(i).get(0)))
                    return false;
            }
            if(extra.get(i).equals(1)){
                if(pack.getTargets().get(i).size() != 1)
                    return false;
                if(!target0.get(0).getPosition().getVisibleCharacters().contains(pack.getTargets().get(i).get(0)))
                    return false;
            }
            if(extra.get(i).equals(2)){
                if(pack.getTargets().get(i).size() != 1)
                    return false;
                if(!target1.get(0).getPosition().getVisibleCharacters().contains(pack.getTargets().get(i).get(0)))
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
        this.effectsnumber = pack.getExtra().size();
        this.extraPosition0 = pack.getExtra().get(0);
        this.extraPosition1 = pack.getExtra().get(1);
        this.extraPosition2 = pack.getExtra().get(2);
        this.target0 = pack.getTargets().get(0);
        this.target1 = pack.getTargets().get(1);
        this.playerPositionVisibleCharacters = pack.getPlayer().getPosition().getVisibleCharacters();
        this.extra = pack.getExtra();
    }
}
