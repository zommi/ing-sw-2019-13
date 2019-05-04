package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;

import java.util.List;

public class Zx2 implements WeaponRulesInterface{

    private int effect;
    private PlayerAbstract player;
    private List<GameCharacter> targetsEffect1; //alternative effect
    private GameCharacter targetEffect0; //base effect

    @Override
    public boolean validate(ShootInfo pack) {
        unpack(pack);
        if(pack.getTargets().size() != 1)
            return false;
        switch(effect){
            case 0:
                return player.getPosition()
                        .getVisibleCharacters().contains(targetEffect0)
                            && pack.getTargets().get(0).size() != 1;
            case 1:
                if(targetsEffect1.size() > 3 || targetsEffect1.isEmpty())
                    return false;
                for(int i=0; i<targetsEffect1.size(); i++) {
                    for (int j = i + 1; j < targetsEffect1.size(); j++) {
                        if (targetsEffect1.get(i).equals(targetsEffect1.get(j)))
                            return false;
                    }
                    if(!player.getPosition().getVisibleCharacters().contains(targetsEffect1.get(i)))
                        return false;
                }
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
        if(effect == 0){        //TODO add 0 as constant: ALT_EFFECT_POS
            targetEffect0 = pack.getTargets().get(0).get(0);
        }
        if(effect == 1)
            targetsEffect1 = pack.getTargets().get(0);

    }
}
