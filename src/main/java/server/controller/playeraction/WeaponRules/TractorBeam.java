package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.map.SquareAbstract;
import server.model.player.*;
import server.model.player.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class TractorBeam implements WeaponRulesInterface{

    private SquareAbstract playerPosition;
    private ArrayList<GameCharacter> zero;
    private ArrayList<GameCharacter> one;
    private ArrayList<GameCharacter> two;
    private ArrayList<GameCharacter> three;
    private int effectsnumber;
    private int extraPosition0;
    private List<GameCharacter> target0;
    private List<SquareAbstract> targetSquares;

    @Override
    public boolean validate(ShootInfo pack){

        if((effectsnumber == 0) || (effectsnumber > 1))
            return false;
        if((extraPosition0 != 0) && (extraPosition0 != 1))
            return false;


        if(extraPosition0 == 0){ //effect extra 0
            if((target0.size() > 1) ||
                    ((!zero.contains(target0.get(0)))&&
                    (!one.contains(target0.get(0)))&&
                    (!two.contains(target0.get(0)))&&
                    (!three.contains(target0.get(0)))))
                return false;
            if(!playerPosition.getAdjacentSquares().contains((targetSquares.get(0))))
                return false;
        }
        if(extraPosition0 == 1){
            if ((target0.size() > 1) ||
                    ((!zero.contains(target0.get(0)))&&
                    (!one.contains(target0.get(0)))&&
                    (!two.contains(target0.get(0)))))
                return false;
            if(!playerPosition.equals(targetSquares.get(0)))
                return false;
        }
        return true;
    }



    @Override
    public void actuate(ShootInfo pack){

    }

    @Override
    public void unpack(ShootInfo pack){
        this.effectsnumber = pack.getExtra().size();
        this.zero = (ArrayList<GameCharacter>) pack.getPlayer().getPosition().getCharacters();
        this.one = (ArrayList<GameCharacter>) pack.getPlayer().getPosition().getExactlyOneMovementCharacters();
        this.two = (ArrayList<GameCharacter>) pack.getPlayer().getPosition().getExactlyTwoMovementsCharacters();
        this.three = (ArrayList<GameCharacter>) pack.getPlayer().getPosition().getExactlyThreeMovementsCharacters();
        this.extraPosition0 = pack.getExtra().get(0);
        this.target0 = pack.getTargets().get(0);
        this.playerPosition = pack.getPlayer().getPosition();
        this.targetSquares = pack.getTargetSquares();
    }
}
