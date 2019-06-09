package client.weapons;

import constants.Color;
import server.model.cards.PowerUpCard;

public class ScopePack {
    private PowerUpCard targetingScope;
    private String target;
    private Color color;

    public ScopePack(PowerUpCard targetingScope, String target, Color color){
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

    public String getTarget() {
        return target;
    }
}
