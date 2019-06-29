package client.powerups;

import client.GameModel;
import client.InputAbstract;
import client.SquareInfo;
import server.model.cards.*;
import server.model.map.Square;

public class PowerUpParser {

    private InputAbstract input;
    private GameModel gameModel;

    public PowerUpParser(InputAbstract input){
        this.input = input;
    }

    public PowerUpPack getPowerUpInput(PowerUpCard powerUpCard){
        if(powerUpCard.getPowerUp() instanceof Newton){
            String target = input.askPlayers(1).get(0);
            SquareInfo squareInfo = input.askSquares(1).get(0);
            return new PowerUpPack(powerUpCard, squareInfo, target);
        }
        else if(powerUpCard.getPowerUp() instanceof Teleporter){
            SquareInfo squareInfo = input.askSquares(1).get(0);
            return new PowerUpPack(powerUpCard, squareInfo, null);
        }
        else if(powerUpCard.getPowerUp() instanceof TagbackGrenade){
            return new PowerUpPack(powerUpCard, null, null);
        }
        else return null;
    }
}
