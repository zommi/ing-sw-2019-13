package server.controller.playeraction;

import constants.Color;
import server.model.cards.PowerUpCard;
import server.model.player.PlayerAbstract;

public class ScopeInfo {
    private PowerUpCard targetingScope;
    private PlayerAbstract target;
    private Color color;

    public ScopeInfo(PowerUpCard targetingScope, PlayerAbstract target, Color color){
        this.targetingScope = targetingScope;
        this.target = target;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public PowerUpCard getTargetingScope() {
        return targetingScope;
    }

    public PlayerAbstract getTarget() {
        return target;
    }
}
