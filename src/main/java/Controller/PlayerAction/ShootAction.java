package Controller.PlayerAction;

import Model.Cards.*;
import Constants.Color;
import Constants.Directions;
import Model.Player.PlayerAbstract;

import java.util.List;

public class ShootAction implements Action{
    private WeaponCard weapon;
    private List<PlayerAbstract> targets;
    private Color playerColor;
    private List<Directions> move;
    private PlayerAbstract player;
    private int extra;

    public ShootAction(ActionInfo info){
        this.player = info.getPlayer();
        this.move = info.getMoves();
        this.weapon = info.getWeapon();
        this.targets = info.getTargets();
        this.playerColor = info.getPlayerColor();
        this.extra = info.getExtra();
    }

    @Override
    public void execute() {
        this.shoot();
    }

    public void shoot(){
        for(Directions dir : move){
            player.move(dir);
        }
        /*
        for(PlayerAbstract target : targets){
            target.receiveBullet(weapon.play(this.extra),playerColor);
        }
         */
    }
}
