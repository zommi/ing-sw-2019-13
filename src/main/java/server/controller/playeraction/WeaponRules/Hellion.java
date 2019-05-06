package server.controller.playeraction.WeaponRules;

import server.controller.playeraction.ShootInfo;
import server.model.items.AmmoCube;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;

import java.util.ArrayList;

public class Hellion implements WeaponRulesInterface{
    private boolean basicModeFlag;
    private SquareAbstract playerPosition;
    private GameCharacter targetBaseDamage;
    private ArrayList<GameCharacter> targetsMarks;
    private ArrayList<AmmoCube> costSecondMode;
    private boolean canPay;

    @Override
    public boolean validate(ShootInfo pack) {
        unpack(pack);

        if(playerPosition.getCharacters().contains(targetBaseDamage) &&
                !playerPosition.getVisibleCharacters().contains(targetBaseDamage)){
            return false;
        }

        SquareAbstract targetSquare = targetBaseDamage.getPosition();
        for(GameCharacter player : targetsMarks){
            if(!player.getPosition().equals(targetSquare)){
                return false;
            }
        }

        //the second part is true if the basic mode is activated, if the alternative
        //mode is activated then the player needs to be able to pay the cost.
        return targetsMarks.contains(targetBaseDamage) && (basicModeFlag || canPay);

    }


    @Override
    public void actuate(ShootInfo pack) {

    }


    @Override
    public void unpack(ShootInfo pack) {
        this.basicModeFlag = pack.getExtra().get(0) == 0;
        //the first list is the target that will receive the damage
        this.targetBaseDamage = pack.getTargets().get(0).get(0);
        //the second list contains the targets that will receive the marks
        this.targetsMarks = pack.getTargets().get(1);
        this.playerPosition = pack.getPlayer().getPosition();
        this.canPay = pack.getPlayer().canPay(pack.getWeapon().getCost1());
    }
}
