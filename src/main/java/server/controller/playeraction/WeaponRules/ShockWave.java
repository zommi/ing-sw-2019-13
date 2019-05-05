package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.controller.playeraction.TargetPack;
import server.model.game.Game;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;

import java.util.List;

public class ShockWave implements WeaponRulesInterface{

    private int effect;
    private PlayerAbstract player;
    private List<GameCharacter> targets;

    @Override
    public boolean validate(ShootInfo pack) {
        unpack(pack);
        if(targets.isEmpty() || targets.size() > 3 || !TargetPack.areDifferentTargets(targets))
            return false;
        for(GameCharacter gc : targets){
            if(!player.getPosition().getExactlyOneMovementCharacters().contains(gc))
                return false;
        }
        switch(effect){
            case 0:
                for(int i=0; i<targets.size(); i++){
                    for(int j=i+1; j<targets.size(); j++){
                        if(targets.get(i).getPosition().equals(targets.get(j).getPosition()))
                            return false;
                    }
                }
                return true;
            case 1:
                return true;
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
        targets = pack.getTargets().get(0);
    }
}
