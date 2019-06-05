package server.controller.playeraction.normalaction;

import client.weapons.ShootPack;
import server.controller.Controller;
import server.controller.playeraction.*;
import server.model.game.Game;
import server.model.gameboard.GameBoard;
import server.model.player.PlayerAbstract;

public class ShootAction implements Action {

    private ShootPack shootPack;

    private PlayerAbstract player;
    private Game game;
    ShootValidator shootValidator;
    ShootActuator shootActuator;

    public ShootAction(ShootPack shootPack, PlayerAbstract player, Game game){
        this.shootPack = shootPack;
        this.player = player;
        this.game = game;
        this.shootValidator = new ShootValidator();
        this.shootActuator = new ShootActuator();
    }

    @Override
    public boolean execute(Controller controller) {
        return this.shoot();
    }

    public boolean shoot(){
        ShootValidator shootValidator = new ShootValidator();
        ShootActuator shootActuator = new ShootActuator();
        ShootInfo shootInfo = shootValidator.validate(shootPack, game, player);
        if(shootInfo != null) {
            shootActuator.actuate(shootInfo);
            return true;
        }
        else
            return false;
    }


}
