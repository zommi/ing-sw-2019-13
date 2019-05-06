package server.controller.playeraction.WeaponRules;

import constants.Constants;
import server.controller.playeraction.ShootInfo;
import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class LockRifle implements WeaponRulesInterface{

    private List<GameCharacter> playerPositionVisibleCharacters;
    private List<GameCharacter> target0;
    private List<GameCharacter> target1;
    private int extraPosition0;
    private int extraPosition1;
    private int effectsnumber;

    @Override
    public boolean validate(ShootInfo pack) {

        if((effectsnumber == 0) || (effectsnumber > 2))
            return false;

        if ((effectsnumber == 2) && ((extraPosition0 != 0) || (extraPosition1 != 1)))
            return false;

        if ((effectsnumber == 1) && (extraPosition0 != 0))
            return false;


        for(int i = 0; i < effectsnumber; i++) {
            if(pack.getExtra().get(i).equals(0) && (target0.size() > 1 || !(playerPositionVisibleCharacters.contains(target0.get(0))))) //only basic effect
                    return false;
            // now the extra1 effect
            else if(pack.getExtra().get(i).equals(1) && (target1.size() > 1 || !(playerPositionVisibleCharacters.contains(target1.get(0)))))
                    return false;
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
        this.playerPositionVisibleCharacters = pack.getPlayer().getPosition().getVisibleCharacters();
        this.target0 = pack.getTargets().get(0);
        this.target1 = pack.getTargets().get(1);

    }
}
