package server.controller.playeraction;

import client.powerups.PowerUpPack;
import client.weapons.Cost;
import constants.Constants;
import server.controller.turns.TurnPhase;
import server.model.cards.PowerUpCard;
import server.model.game.Game;
import server.model.gameboard.GameBoard;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PowerUpValidator {
    public PowerUpInfo validate(PowerUpPack powerUpPack, Game game, PlayerAbstract attacker){

        PowerUpInfo powerUpInfo = convert(powerUpPack, game, attacker);
        if(powerUpInfo == null)
            return null;

        switch(powerUpInfo.getPowerUpCard().getPowerUp().getName()){

            case "Newton":
                if(powerUpInfo.getTarget() == null || powerUpInfo.getSquare() == null )
                    return null;
                if(powerUpInfo.getTarget().getPosition().distance(powerUpInfo.getSquare()) > Constants.NEWTON_MAX_DISTANCE
                    || powerUpInfo.getTarget().getPosition() == powerUpInfo.getSquare())
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
                if(game.getTurnHandler().getCurrentPhase() != TurnPhase.TAGBACK_PHASE)
                    return null;

                if(attacker.getJustDamagedBy() == null)
                    return null;

                if(!attacker.getPosition().getVisibleCharacters().contains(attacker.getJustDamagedBy().getGameCharacter()))
                    return null;

                powerUpInfo.setTarget(attacker.getJustDamagedBy());

                return powerUpInfo;

            default: return null;
        }
        return powerUpInfo;

    }

    public PowerUpInfo convert(PowerUpPack powerUpPack, Game game, PlayerAbstract attacker){
        GameBoard gameBoard = game.getCurrentGameBoard();
        SquareAbstract square = null;
        PlayerAbstract target = null;

        if(powerUpPack == null)
            return null;

        //checking if attacker has the selected card
        if(!attacker.hasPowerUpCards(new ArrayList<>(Collections.singletonList(powerUpPack.getPowerupCard())))){
            return null;
        }

        if(powerUpPack.getSquareInfo() != null && gameBoard.getMap().getSquare(powerUpPack.getSquareInfo().getRow(),
                powerUpPack.getSquareInfo().getCol()) != null)
            square = gameBoard.getMap().getSquare(powerUpPack.getSquareInfo().getRow(),
                    powerUpPack.getSquareInfo().getCol());

        if(powerUpPack.getTarget() != null && game.getPlayer(powerUpPack.getTarget()) != null)
            target = game.getPlayer(powerUpPack.getTarget());

        return new PowerUpInfo(attacker, powerUpPack.getPowerupCard(), square, target);
    }

}
