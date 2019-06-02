package server.controller.playeraction.normalaction;

import client.weapons.ShootPack;
import server.controller.playeraction.*;
import server.model.gameboard.GameBoard;
import server.model.player.PlayerAbstract;

public class ShootAction implements Action {

    private ShootPack shootPack;

    private PlayerAbstract player;
    private GameBoard gameBoard;
    private ShootValidator validator;
    private ShootActuator actuator;

    public ShootAction(ShootPack shootPack, PlayerAbstract player, GameBoard gameBoard){
        this.shootPack = shootPack;
        this.player = player;
        this.gameBoard = gameBoard;
    }

    @Override
    public boolean execute() {
        return this.shoot();
    }

    public boolean shoot(){
        ShootInfo shootInfo = validator.validate(shootPack, gameBoard, player);
        if(shootInfo != null) {
            actuator.actuate(shootInfo);
            return true;
        }
        else
            return false;
    }


}
