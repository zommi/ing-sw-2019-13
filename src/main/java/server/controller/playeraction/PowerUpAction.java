package server.controller.playeraction;

import client.powerups.PowerUpPack;
import server.controller.Controller;
import server.model.game.Game;
import server.model.player.PlayerAbstract;

public class PowerUpAction implements Action {


    private PowerUpPack powerUpPack;
    private PowerUpValidator validator;
    private PowerUpActuator actuator;
    private PlayerAbstract player;

    public PowerUpAction(PowerUpPack powerUpPack, Game game, PlayerAbstract player){
        this.powerUpPack = powerUpPack;
        this.validator = new PowerUpValidator();
        this.actuator = new PowerUpActuator();
        this.player = player;
    }

    @Override
    public boolean execute(Controller controller) {
        //TODO
        return this.usePowerUp(controller);
    }

    public boolean usePowerUp(Controller controller){
        PowerUpInfo powerUpInfo = validator.validate(powerUpPack, controller.getCurrentGame(), player);
        if(powerUpInfo != null) {
            actuator.actuate(powerUpInfo);
            return true;
        }else {
            //System.out.println("You can't collect");
            return false;
        }
    }
}
