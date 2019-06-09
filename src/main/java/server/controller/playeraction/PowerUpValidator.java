package server.controller.playeraction;

import client.powerups.PowerUpPack;
import client.weapons.Cost;
import server.model.cards.PowerUpCard;
import server.model.game.Game;
import server.model.gameboard.GameBoard;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

public class PowerUpValidator {
    public PowerUpInfo validate(PowerUpPack powerUpPack, Game game, PlayerAbstract attacker){
        PowerUpInfo powerUpInfo = convert(powerUpPack, game, attacker);
        if(powerUpInfo == null)
            return null;

        //checks if players owns the chosen card
        boolean found = false;
        for(PowerUpCard powerUpCard : attacker.getHand().getPowerupHand()){
            if(powerUpCard.equals(powerUpPack.getPowerupCard()))
                found = true;
        }
        if(!found)
            return null;


        switch(powerUpInfo.getPowerUpCard().getPowerUp().getName()){
            case "Newton":
                if(powerUpInfo.getTarget() == null || powerUpInfo.getSquare() == null )
                    return null;
                if(powerUpInfo.getTarget().getPosition().distance(powerUpInfo.getSquare()) > 2
                    ||powerUpInfo.getTarget().getPosition() == powerUpInfo.getSquare())
                    return null;
                if(powerUpInfo.getTarget().getPosition().getRow()!=powerUpInfo.getSquare().getRow() &&
                        powerUpInfo.getTarget().getPosition().getCol()!=powerUpInfo.getSquare().getCol())
                    return null;
                return powerUpInfo;
            case "Teleporter":
                if(powerUpInfo.getSquare() == null)
                    return null;
                if(powerUpInfo.getTarget() != null)
                    return null;
                powerUpInfo.setTarget(attacker);
                break;
            case "Tagback Grenade":
                if(powerUpInfo.getTarget() != null)
                    return null;
                powerUpInfo.setTarget(attacker.getJustDamagedBy());
                if(!attacker.getPosition().getVisibleCharacters().contains(powerUpInfo
                        .getTarget().getGameCharacter()))
                    return null;
                return powerUpInfo;
            default: return null;
        }
        return null;

    }

    public PowerUpInfo convert(PowerUpPack powerUpPack, Game game, PlayerAbstract attacker){
        GameBoard gameBoard = game.getCurrentGameBoard();
        SquareAbstract square = null;
        PlayerAbstract target = null;

        if(powerUpPack == null)
            return null;

        if(powerUpPack.getSquareInfo() != null && gameBoard.getMap().getSquare(powerUpPack.getSquareInfo().getRow(),
                powerUpPack.getSquareInfo().getCol()) != null)
            square = gameBoard.getMap().getSquare(powerUpPack.getSquareInfo().getRow(),
                    powerUpPack.getSquareInfo().getCol());

        if(powerUpPack.getTarget() != null && game.getPlayer(powerUpPack.getTarget()) != null)
            target = game.getPlayer(powerUpPack.getTarget());

        return new PowerUpInfo(attacker, powerUpPack.getPowerupCard(), square, target);
    }

}
