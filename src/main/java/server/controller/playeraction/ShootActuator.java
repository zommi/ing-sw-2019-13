package server.controller.playeraction;


import client.weapons.Cost;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.List;

public class ShootActuator {
    public void actuate(ShootInfo shootInfo){
        //actuate
        for(MacroInfo macroInfo : shootInfo.getActivatedMacros()){
            for(MicroInfo microInfo : macroInfo.getActivatedMicros())
                microInfo.actuate(shootInfo);
        }

        //pay
        shootInfo.getAttacker().payWithPowerUps(shootInfo.getTotalCost(), shootInfo.getPowerUpCards());

        //adding unloaded weapon to the playerboard, so that it's visible to the other players
        //but keeping it in playerhand
        WeaponCard weaponCard = shootInfo.getAttacker().getWeaponCard(shootInfo.getWeapon());
        weaponCard.setReady(false);
        shootInfo.getAttacker().getPlayerBoard().addUnloadedWeapon(weaponCard);
    }
}
