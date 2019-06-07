package server.controller.playeraction;




public class PowerUpActuator {

    private static final int TARGETING_SCOPE_DAMAGE = 1;
    private static final int TAGBACK_GRENADE_MARKS = 1;

    public void actuate(PowerUpInfo powerUpInfo){
        switch(powerUpInfo.getPowerUpCard().getPowerUp().getName()){
            case "Targeting Scope":
                powerUpInfo.getAttacker().pay(powerUpInfo.getCost());
                powerUpInfo.getTarget().addDamage(TARGETING_SCOPE_DAMAGE, powerUpInfo.getAttacker().getColor());
                break;
            case "Newton":
                powerUpInfo.getTarget().getGameCharacter().move(powerUpInfo.getSquare());
                break;
            case "Teleporter":
                powerUpInfo.getAttacker().getGameCharacter().move(powerUpInfo.getSquare());
                break;
            case "Tagback Grenade":
                powerUpInfo.getTarget().addMarks(TAGBACK_GRENADE_MARKS, powerUpInfo.getAttacker().getColor());
                break;
            default: //this should never happen
        }

        //discard
        powerUpInfo.getAttacker().getHand().removePowerUpCard(powerUpInfo.getPowerUpCard());
    }
}
