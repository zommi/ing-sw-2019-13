package server.controller.playeraction;


import constants.Constants;

public class PowerUpActuator {


    public void actuate(PowerUpInfo powerUpInfo){
        switch(powerUpInfo.getPowerUpCard().getPowerUp().getName()){
            case "Newton":
                powerUpInfo.getTarget().getGameCharacter().move(powerUpInfo.getSquare());
                break;
            case "Teleporter":
                powerUpInfo.getAttacker().getGameCharacter().move(powerUpInfo.getSquare());
                break;
            case "Tagback Grenade":
                powerUpInfo.getTarget().addMarks(Constants.TAGBACK_GRENADE_MARKS, powerUpInfo.getAttacker().getColor());
                break;
            default: //this should never happen
        }

        //discard
        powerUpInfo.getAttacker().getHand().removePowerUpCard(powerUpInfo.getPowerUpCard());
    }
}
