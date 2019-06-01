package client.powerups;

import client.Info;
import client.SquareInfo;
import constants.Color;
import server.model.cards.PowerUpCard;

public class PowerUpPack implements Info {
    private PowerUpCard powerUpCard;
    private SquareInfo squareInfo;
    private String target;
    private Color cubeColor;

    public SquareInfo getSquareInfo() {
        return squareInfo;
    }

    public String getTarget() {
        return target;
    }

    public PowerUpCard getPowerupCard() {
        return powerUpCard;
    }

    public Color getCubeColor() {
        return cubeColor;
    }
}
