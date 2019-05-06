package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.List;

public class PlasmaGun implements WeaponRulesInterface{

    private int effectsnumber;
    private int extra0;
    private int extra1;
    private List<GameCharacter> target0;
    private List<GameCharacter> target1;
    private List<GameCharacter> playerPositionVisibleCharacters;
    private List<SquareAbstract> playerPositionOneMovement;
    private List<SquareAbstract> playerPositionTwoMovement;


    @Override
    public boolean validate(ShootInfo pack){

        if((effectsnumber == 0)||(effectsnumber > 3))
            return false;

        if((effectsnumber == 3)||(effectsnumber == 2)){
            if(extra0 == 2)
                return false;
            if((extra1 == 2) && (extra0 == 1))
                return false;
        }
        if((effectsnumber == 1)&&(extra0 == 2)){
            return false;
        }


        for(int i = 0; i < effectsnumber; i++) {
            if(pack.getExtra().get(i).equals(0)){
                if(pack.getTargets().get(i).size() != 1)
                    return false; //only one target
                if(!playerPositionVisibleCharacters.contains(pack.getTargets().get(i).get(0)))
                    return false;
            }

            if(pack.getExtra().get(i).equals(1)){
                if(!playerPositionOneMovement.contains(pack.getYourSquares().get(0)) &&
                        (!playerPositionTwoMovement.contains(pack.getYourSquares().get(0))))
                    return false;
            }

            if(pack.getExtra().get(i).equals(2)){
                if(target1.size() != 1) //it will be the number 1 for sure: the second extra does not prepare any list of targets
                    return false; //only one target
                if(!target0.get(0).equals(target1.get(0)))
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
        this.effectsnumber = pack.getExtra().size();
        this.extra0 = pack.getExtra().get(0);
        this.extra1 = pack.getExtra().get(1);
        this.playerPositionVisibleCharacters = pack.getPlayer().getPosition().getVisibleCharacters();
        this.playerPositionOneMovement = pack.getPlayer().getPosition().getAdjacentSquares();
        this.playerPositionTwoMovement = pack.getPlayer().getPosition().getExactlyTwoMovementsSquares();
        this.target0 = pack.getTargets().get(0);
        this.target1 = pack.getTargets().get(1);
    }
}
