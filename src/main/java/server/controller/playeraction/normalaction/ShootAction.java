package server.controller.playeraction.normalaction;

import server.controller.playeraction.Action;
import server.controller.playeraction.MoveInfo;
import server.controller.playeraction.ShootInfo;
import server.model.cards.*;
import constants.Color;
import constants.Directions;
import server.model.player.PlayerAbstract;

import java.util.List;

public class ShootAction implements Action {
    private WeaponCard weapon;
    private List<PlayerAbstract> targets;
    private PlayerAbstract player;
    private int extra;
    private int xAfterPush;
    private int yAfterPush;

    public ShootAction(ShootInfo info){
        this.player = info.getPlayer();
        this.weapon = info.getWeapon();
        this.targets = info.getTargets();
        this.extra = info.getExtra();
        this.xAfterPush = info.getxAfterPush();
        this.yAfterPush = info.getyAfterPush();
    }

    @Override
    public void execute() {
        this.shoot();
    }

    public void shoot(){
        List<Bullet> listOfBullets = weapon.play(extra,xAfterPush,yAfterPush);
        for(int i = 0; i < targets.size(); i++){
            targets.get(i).receiveBullet(listOfBullets.get(i),player.getColor());
        }
    }
}
