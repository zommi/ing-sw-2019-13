package server.controller.playeraction;

import client.MoveInfo;
import client.powerups.PowerUpPack;
import server.model.gameboard.GameBoard;
import server.model.player.PlayerAbstract;

import java.util.Map;

public class PowerUpAction implements Action {


    private PowerUpPack powerUpPack;
    private PowerUpValidator validator;
    //private PowerUpActuator actuator;
    private PlayerAbstract player;
    private GameBoard gameBoard;

    public PowerUpAction(PowerUpPack powerUpPack, GameBoard gameBoard, PlayerAbstract player){
        this.powerUpPack = powerUpPack;
        this.validator = new PowerUpValidator();
        //this.actuator = new PowerUpActuator();
        this.gameBoard = gameBoard;
        this.player = player;
    }

    @Override
    public boolean execute() {
        //TODO
        return this.usePowerUp();
    }

    public boolean usePowerUp(){
        //if(validator.validate(powerUpPack, gameBoard, player)) {
            //actuator.actuate(player, );
            return true;
        /*}else {
            System.out.println("You can't collect");
            return false;
        }*/
    }
}
