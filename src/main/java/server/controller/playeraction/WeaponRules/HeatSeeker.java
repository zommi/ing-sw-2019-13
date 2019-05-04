package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

public class HeatSeeker implements WeaponRulesInterface{
    private SquareAbstract playerPosition;
    private GameCharacter target;

    @Override
    public boolean validate(ShootInfo pack) {
        unpack(pack);
        return !playerPosition.getVisibleCharacters().contains(target);
    }

    @Override
    public void actuate(ShootInfo pack) {

    }

    @Override
    public void unpack(ShootInfo pack) {
        this.playerPosition = pack.getPlayer().getPosition();
        this.target = pack.getTargets().get(0).get(0);
    }
}
