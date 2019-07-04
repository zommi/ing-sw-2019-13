package server.controller.playeraction;


import constants.Constants;
import server.controller.Controller;
import server.model.cards.WeaponCard;
import view.MessageAnswer;

/**
 * It is used to create an actuator and actuate a particular shoot info
 * @author Matteo Pacciani
 */
public class ShootActuator {
    /**
     * Reloads the weapon of the shoot info if it is legal, actuate every micro effect included in the
     * shoot info, dealing damage, marks and moving the players in every micro effect, according to the game's
     * rules. Deals damage of every activated targeting scopes, removing them from the player hand.
     * Sets the weapon used to unloaded, adding it to the playerboard so that it is visible to all the
     * other players, removes all the powerups used for paying this action.
     * Sends a message to all the other players.
     *
     * @param shootInfo the shoot info that is going to be actuated
     * @param controller the controller of the corresponding game
     */
    public void actuate(ShootInfo shootInfo, Controller controller){

        //final frenzy and reload
        if(shootInfo.getReloadInfo() != null && controller != null) {
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
            shootInfo.getAttacker().getHand().removePowerUpCard(scopeInfo.getTargetingScope());
        }

        if(shootInfo.getSquare() != null)
            shootInfo.getAttacker().getGameCharacter().move(shootInfo.getSquare());

        //pay
        shootInfo.getAttacker().payWithPowerUps(shootInfo.getTotalCost(), shootInfo.getPowerUpCards());

        //adding unloaded weapon to the playerboard, so that it's visible to the other players
        //but keeping it in playerhand
        WeaponCard weaponCard = shootInfo.getAttacker().getWeaponCard(shootInfo.getWeapon());
        weaponCard.setReady(false);
        shootInfo.getAttacker().getPlayerBoard().addUnloadedWeapon(weaponCard);

        if(controller != null) {
            String message = shootInfo.getAttacker().getName() + " shot with " + shootInfo.getWeapon().getName();
            controller.getGameManager().sendEverybodyExcept(new MessageAnswer(message), shootInfo.getAttacker().getClientID());

            if(shootInfo.getSquare() != null){
                String message1 = shootInfo.getAttacker().getName() + " moved while shooting, he's sooo smart";
                controller.getGameManager().sendEverybodyExcept(new MessageAnswer(message1), shootInfo.getAttacker().getClientID());
            }
        }
    }
}
