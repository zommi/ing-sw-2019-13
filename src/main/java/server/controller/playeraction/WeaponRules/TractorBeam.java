package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.player.*;
import server.model.player.GameCharacter;

import java.util.ArrayList;

public class TractorBeam implements WeaponRulesInterface{

    @Override
    public boolean validate(ShootInfo pack){
        int effectsnumber = pack.getExtra().size();

        ArrayList<GameCharacter> zero;
        ArrayList<GameCharacter> one;
        ArrayList<GameCharacter> two;
        ArrayList<GameCharacter> three;
        zero = (ArrayList<GameCharacter>) pack.getPlayer().getPosition().getCharacters();
        one = (ArrayList<GameCharacter>) pack.getPlayer().getPosition().getExactlyOneMovementCharacters();
        two = (ArrayList<GameCharacter>) pack.getPlayer().getPosition().getExactlyTwoMovementsCharacters();
        three = (ArrayList<GameCharacter>) pack.getPlayer().getPosition().getExactlyThreeMovementsCharacters();

        if(effectsnumber == 0)
            return false;
        if (effectsnumber > 1)
            return false;
        if(pack.getExtra().get(0) != 0 && pack.getExtra().get(0) != 1)
            return false;


        if(pack.getExtra().get(0) == 0){ //effect extra 0
            if((pack.getTargets().get(0).size() > 1) ||
                    ((!zero.contains(pack.getTargets().get(0).get(0)))&&
                    (!one.contains(pack.getTargets().get(0).get(0)))&&
                    (!two.contains(pack.getTargets().get(0).get(0)))&&
                    (!three.contains(pack.getTargets().get(0).get(0)))))
                return false;
            if(!pack.getPlayer().getPosition().getAdjacentSquares().contains((pack.getTargetSquares().get(0))))
                return false;
        }
        else if(pack.getExtra().get(0) == 1){
            if ((pack.getTargets().get(0).size() > 1) ||
                    ((!zero.contains(pack.getTargets().get(0).get(0)))&&
                    (!one.contains(pack.getTargets().get(0).get(0)))&&
                    (!two.contains(pack.getTargets().get(0).get(0)))))
                return false;
            if(!pack.getPlayer().getPosition().equals(pack.getTargetSquares().get(0)))
                return false;
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
