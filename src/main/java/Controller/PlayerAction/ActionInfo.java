package Controller.PlayerAction;

import Model.Cards.*;
import Constants.*;
import Model.Player.PlayerAbstract;

import java.util.List;

/**
 * Object created by the observer that tells the player what kind of action the player
 * inputted in the view
 */
public class ActionInfo {
    private WeaponCard weapon;
    private List<Directions> moves;
    private List<PlayerAbstract> targets;
    private Color playerColor;
    private PlayerAbstract player;
    private int extra;

    public ActionInfo(){
        this.player = null;
        this.weapon = null;
        this.moves = null;
        this.targets = null;
        this.playerColor = null;
    }

    public ActionInfo(PlayerAbstract player, List<Directions> moves){
        this.player = player;
        this.weapon = null;
        this.moves = moves;
        this.targets = null;
    }

    public  ActionInfo(PlayerAbstract player, List<PlayerAbstract> targets,
                       WeaponCard weapon, List<Directions> moves, int extra, Color playerColor){
        this.player = player;
        this.weapon = weapon;
        this.moves = moves;
        this.targets = targets;
        this.playerColor = playerColor;
        this.extra = extra;
    }

    public PlayerAbstract getPlayer(){ return this.player;}

    public WeaponCard getWeapon() {
        return this.weapon;
    }

    public List<Directions> getMoves() {
        return this.moves;
    }

    public List<PlayerAbstract> getTargets(){
        return targets;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public int getExtra() {
        return this.extra;
    }
}
