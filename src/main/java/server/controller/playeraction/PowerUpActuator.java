package server.controller.playeraction;


import constants.Constants;
import server.controller.Controller;
import answers.MessageAnswer;

public class PowerUpActuator {


    public void actuate(PowerUpInfo powerUpInfo, Controller controller){
        String powerUpName = powerUpInfo.getPowerUpCard().getPowerUp().getName();

        switch(powerUpName){
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

        //sending message
        String message = powerUpInfo.getAttacker().getName() + " used " + powerUpInfo.getPowerUpCard().getName() +
                (powerUpName.equalsIgnoreCase("Teleporter") ?
                        "" : (" against " + powerUpInfo.getTarget().getName()));
        controller.getGameManager().sendEverybodyExcept(new MessageAnswer(message), powerUpInfo.getAttacker().getClientID());

    }
}
