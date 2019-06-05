package server.controller.playeraction.normalaction;

import client.weapons.ShootPack;
import server.controller.Controller;
import server.controller.playeraction.*;
import server.model.gameboard.GameBoard;
import server.model.player.PlayerAbstract;

public class ShootAction implements Action {

    private ShootPack shootPack;

    private PlayerAbstract player;
    private GameBoard gameBoard;

    public ShootAction(ShootPack shootPack, PlayerAbstract player, GameBoard gameBoard){
        this.shootPack = shootPack;
        this.player = player;
        this.gameBoard = gameBoard;
    }

    @Override
    public boolean execute(Controller controller) {
        return this.shoot();
    }

    public boolean shoot(){
        ShootValidator shootValidator = new ShootValidator();
        ShootActuator shootActuator = new ShootActuator();
        ShootInfo shootInfo = shootValidator.validate(shootPack, gameBoard, player);
        if(shootInfo != null) {
            shootActuator.actuate(shootInfo);
            return true;
        }
        else
            return false;
    }


}
