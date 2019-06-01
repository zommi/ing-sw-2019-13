package server.controller.playeraction;

import constants.Color;
import server.model.cards.PowerUpCard;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

public class PowerUpInfo {
    private PlayerAbstract attacker;
    private PowerUpCard powerUpCard;
    private SquareAbstract square;
    private PlayerAbstract target;
    private Color cubeColor;

    public PowerUpInfo(PlayerAbstract attacker, PowerUpCard powerUpCard, SquareAbstract square, PlayerAbstract target, Color cubeColor){
        this.attacker = attacker;
        this.powerUpCard = powerUpCard;
        this.square = square;
        this.target = target;
        this.cubeColor = cubeColor;
    }

    public PlayerAbstract getAttacker() {
        return attacker;
    }

    public PlayerAbstract getTarget() {
        return target;
    }

    public PowerUpCard getPowerUpCard() {
        return powerUpCard;
    }

    public SquareAbstract getSquare() {
        return square;
    }

    public void setTarget(PlayerAbstract target) {
        this.target = target;
    }

    public Color getCubeColor() {
        return cubeColor;
    }
}
