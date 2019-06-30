package server.controller.playeraction;


import constants.Constants;
import server.controller.Controller;
import server.model.cards.WeaponCard;
import view.MessageAnswer;

public class ShootActuator {
    public void actuate(ShootInfo shootInfo, Controller controller){

        //final frenzy and reload
        if(shootInfo.getReloadInfo() != null) {
            ReloadActuator reloadActuator = new ReloadActuator();
            reloadActuator.actuate(shootInfo.getReloadInfo(), shootInfo.getAttacker(), controller);
        }

        //actuate
        for(MacroInfo macroInfo : shootInfo.getActivatedMacros()){
            for(MicroInfo microInfo : macroInfo.getActivatedMicros())
                microInfo.actuate(shootInfo);
        }

        //actuate targeting scopes
        for(ScopeInfo scopeInfo : shootInfo.getScopeInfos()){
            scopeInfo.getTarget().addDamage(Constants.TARGET_SCOPE_DMG, shootInfo.getAttacker().getColor());
        }

        //pay
        shootInfo.getAttacker().payWithPowerUps(shootInfo.getTotalCost(), shootInfo.getPowerUpCards());

        //adding unloaded weapon to the playerboard, so that it's visible to the other players
        //but keeping it in playerhand
        WeaponCard weaponCard = shootInfo.getAttacker().getWeaponCard(shootInfo.getWeapon());
        weaponCard.setReady(false);
        shootInfo.getAttacker().getPlayerBoard().addUnloadedWeapon(weaponCard);

        String message = shootInfo.getAttacker().getName() + " shot with " + shootInfo.getWeapon().getName();
        controller.getGameManager().sendEverybodyExcept(new MessageAnswer(message), shootInfo.getAttacker().getClientID());
    }
}
