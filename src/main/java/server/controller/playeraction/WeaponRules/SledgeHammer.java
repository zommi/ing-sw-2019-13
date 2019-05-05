package server.controller.playeraction.WeaponRules;

import constants.Directions;
import server.controller.playeraction.ShootInfo;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;

public class SledgeHammer implements WeaponRulesInterface{

    private int effect;
    private PlayerAbstract player;
    private GameCharacter target; //same for both the effects
    private SquareAbstract targetSquare;

    @Override
    public boolean validate(ShootInfo pack) {
        unpack(pack);
        if(pack.getTargets().size() != 1 || pack.getTargets().get(0).size() != 1)
            return false;
        switch(effect){
            case 0:
                return player.getPosition().getCharacters().contains(target) &&
                        player.getGameCharacter() != target;
            case 1:
                /*
                if player chooses not to move the target, there won't be a target square
                and isSquareOk will be automatically true
                 */
                boolean isSquareOk = false;
                if(pack.getTargetSquares().isEmpty())
                    isSquareOk = true;
                for(Directions dir : Directions.values()){
                    if(player.getPosition().getTwoSquaresInTheSameDirection(dir).contains(targetSquare))
                         isSquareOk = true;
                }
                return player.getPosition().getCharacters().contains(target) &&
                        player.getGameCharacter() != target && isSquareOk;

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
        target = pack.getTargets().get(0).get(0);
        if(!pack.getTargetSquares().isEmpty())
            targetSquare = pack.getTargetSquares().get(0);
    }
}
