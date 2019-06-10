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
    private ShootValidator shootValidator;
    private ShootActuator shootActuator;

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
        ShootInfo shootInfo = shootValidator.validate(shootPack, game, player);
        if(shootInfo != null) {
            shootActuator.actuate(shootInfo);
            return true;
        }
        else
            return false;
    }


}
