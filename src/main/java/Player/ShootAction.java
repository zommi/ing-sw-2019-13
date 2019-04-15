package Player;

import Cards.*;
import Constants.Color;
import Constants.Directions;

import java.util.ArrayList;
import java.util.List;

public class ShootAction implements Action{
    private WeaponCard weapon;
    private List<PlayerAbstract> targets;
    private Color playerColor;
    private List<Directions> move;
    private PlayerAbstract player;

    public ShootAction(ActionInfo info){
        this.player = info.getPlayer();
        this.move = info.getMoves();
        this.weapon = info.getWeapon();
        this.targets = info.getTargets();
        this.playerColor = info.getPlayerColor();
    }

    @Override
    public void execute() {
        this.shoot();
    }

    public void shoot(){
        for(Directions dir : move){
            player.move(dir);
        }
        for(PlayerAbstract target : targets){
            target.receiveBullet(weapon.play(),playerColor);
        }

    }
}
