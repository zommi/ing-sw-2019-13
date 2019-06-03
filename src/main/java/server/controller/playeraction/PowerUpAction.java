package server.controller.playeraction;

import client.powerups.PowerUpPack;
import server.controller.Controller;
import server.model.gameboard.GameBoard;
import server.model.player.PlayerAbstract;

public class PowerUpAction implements Action {


    private PowerUpPack powerUpPack;
    private PowerUpValidator validator;
    private PowerUpActuator actuator;
    private PlayerAbstract player;
    private GameBoard gameBoard;

    public PowerUpAction(PowerUpPack powerUpPack, GameBoard gameBoard, PlayerAbstract player){
        this.powerUpPack = powerUpPack;
        this.validator = new PowerUpValidator();
        this.actuator = new PowerUpActuator();
        this.gameBoard = gameBoard;
        this.player = player;
    }

    @Override
    public boolean execute(Controller controller) {
        //TODO
        return this.usePowerUp(controller);
    }

    public boolean usePowerUp(Controller controller){
        PowerUpInfo powerUpInfo = validator.validate(powerUpPack, gameBoard, player);
        if(powerUpInfo != null) {
            actuator.actuate(powerUpInfo);
            return true;
        }else {
            //System.out.println("You can't collect");
            return false;
        }
    }
}
