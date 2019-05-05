package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.controller.playeraction.TargetPack;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;

import java.util.List;

public class RailGun implements WeaponRulesInterface {

    private int effect;
    private PlayerAbstract player;
    private List<GameCharacter> targetsEffect1; //alternative effect
    private GameCharacter targetEffect0; //base effect
    private GameCharacter target0Effect1;
    private GameCharacter target1Effect1;

    @Override
    public boolean validate(ShootInfo pack) {
        unpack(pack);
        if(pack.getTargets().size() != 1)
            return false;
        switch(effect){
            case 0:
                return (player.getPosition().getCharactersThroughWalls().contains(targetEffect0) &&
                    !player.getGameCharacter().equals(targetEffect0));

            case 1:
                if(targetsEffect1.isEmpty() || targetsEffect1.size() > 2 ||
                        !TargetPack.areDifferentTargets(targetsEffect1) ||
                        player.getPosition().getCharactersThroughWalls().containsAll(targetsEffect1))
                    return false;
                if(targetsEffect1.size() == 1){
                    return !player.getGameCharacter().equals(target0Effect1);
                }
                else{   //size == 2
                    int playerRow = player.getPosition().getRow();
                    int row0 = target0Effect1.getPosition().getRow();
                    int row1 = target1Effect1.getPosition().getRow();
                    int playerCol = player.getPosition().getCol();
                    int col0 = target0Effect1.getPosition().getCol();
                    int col1 = target1Effect1.getPosition().getCol();
                    if(row0 == row1 && ((col0 <= playerCol && col1 <= playerCol) ||
                            (col0 >= playerCol && col1 >= playerCol))){
                            return true;
                    }
                    return (col0 == col1 && ((row0 <= playerRow && row1 <= playerRow) ||
                            (row0 >= playerRow && row1 >= playerRow)));
                }

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

    }

}
