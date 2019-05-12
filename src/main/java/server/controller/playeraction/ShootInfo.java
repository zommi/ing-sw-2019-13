package server.controller.playeraction;

import constants.Color;
import constants.Directions;
import server.model.cards.Weapon;
import server.model.cards.WeaponCard;
import server.model.map.Room;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;
import server.model.cards.*;

import java.util.ArrayList;
import java.util.List;

public class ShootInfo {

    private PlayerAbstract attacker;
    private Weapon weapon;
    private List<MacroInfo> activatedMacros;





    public PlayerAbstract getAttacker() {
        return attacker;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public List<MacroInfo> getActivatedMacros() {
        return activatedMacros;
    }

    public MacroInfo getActivatedMacro(int macro){
        return activatedMacros.get(macro);
    }

    public MicroInfo getActivatedMicro(int macro, int micro){
        return getActivatedMacro(macro).getActivatedMicro(micro);
    }
}
