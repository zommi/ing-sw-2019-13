package server.controller.playeraction;

import constants.Color;
import constants.Directions;
import server.model.cards.WeaponCard;
import server.model.player.PlayerAbstract;

import java.util.List;

public class ShootInfo {

    private PlayerAbstract player;

    private WeaponCard weapon;

    private List<PlayerAbstract> targets;

    private int extra;

    private int xAfterPush;

    private int yAfterPush;

    public  ShootInfo(PlayerAbstract player, List<PlayerAbstract> targets,
                      WeaponCard weapon, int extra, int x, int y) {
        this.player = player;
        this.weapon = weapon;
        this.targets = targets;
        this.extra = extra;
        this.xAfterPush = x;
        this.yAfterPush = y;
    }

    public List<PlayerAbstract> getTargets() {
        return targets;
    }

    public PlayerAbstract getPlayer() {
        return player;
    }

    public WeaponCard getWeapon() {
        return weapon;
    }

    public int getExtra() {
        return extra;
    }

    public int getxAfterPush() {
        return xAfterPush;
    }

    public int getyAfterPush() {
        return yAfterPush;
    }
}
