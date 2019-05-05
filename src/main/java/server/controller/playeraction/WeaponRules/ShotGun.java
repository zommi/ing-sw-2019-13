package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;


public class ShotGun implements WeaponRulesInterface{

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
                        player.getGameCharacter() != target &&
                        player.getPosition().getAdjacentSquares().contains(targetSquare);
            case 1:
                return player.getPosition().getExactlyOneMovementCharacters().contains(target);
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
