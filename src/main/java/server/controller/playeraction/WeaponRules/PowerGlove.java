package server.controller.playeraction.WeaponRules;

import constants.Directions;
import server.controller.playeraction.ShootInfo;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;

import java.util.ArrayList;
import java.util.List;

public class PowerGlove implements WeaponRulesInterface{

    private int effect;
    private PlayerAbstract player;
    private List<GameCharacter> targetsEffect1; //alternative effect
    private GameCharacter targetEffect0; //base effect
    private GameCharacter target0Effect1;
    private GameCharacter target1Effect1;
    private List<SquareAbstract> squaresList;

    @Override
    public boolean validate(ShootInfo pack) {
        this.unpack(pack);
        if(pack.getTargets().size() != 1)
            return false;
        switch (effect){
            case 0:
                return  pack.getTargets().get(0).size() == 1 && pack.getYourSquares().size() == 1 &&
                        player.getPosition().getExactlyOneMovementCharacters().contains(targetEffect0) &&
                        squaresList.get(0).equals(targetEffect0.getPosition());
            case 1:
                if(squaresList.size() != 1 && squaresList.size() != 2){
                    return false;
                }

                //this checks squares relative positions
                boolean areSquaresOk = false;
                for(Directions dir : Directions.values()){
                    if(player.getPosition().getTwoSquaresInTheSameDirection(dir).containsAll(squaresList)) {
                        if (squaresList.size() == 2 && squaresList.get(0).equals(squaresList.get(1)))
                            return false;
                        areSquaresOk = true;
                    }
                }

                //we already know that squares are different, so must be the targets
                for(int i=0; i<targetsEffect1.size(); i++){
                    if(!squaresList.get(i).equals(targetsEffect1.get(i).getPosition())){
                        return false;
                    }
                }

                return areSquaresOk;

            default:
                return false;
        }
    }

    @Override
    public void actuate(ShootInfo pack) {

    }

    @Override
    public void unpack(ShootInfo pack) {
        effect = pack.getExtra().get(0);
        player = pack.getPlayer();
        if (effect == 0) {        //TODO add 0 as constant: ALT_EFFECT_POS
            targetEffect0 = pack.getTargets().get(0).get(0);
        }
        if (effect == 1) {
            targetsEffect1 = pack.getTargets().get(0);
            target0Effect1 = targetsEffect1.get(0);
            if(targetsEffect1.size() == 2){
                target1Effect1 = targetsEffect1.get(1);

            }
        }
        squaresList = pack.getYourSquares();

    }
}
