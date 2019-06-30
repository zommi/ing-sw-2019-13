package server.controller.playeraction;
import client.SpawnInfo;
import server.controller.Controller;
import server.model.cards.PowerUp;
import server.model.cards.PowerUpCard;
import server.model.gameboard.GameBoard;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;
import view.MessageAnswer;

import java.util.ArrayList;
import java.util.List;

public class SpawnAction implements Action {

    private PlayerAbstract player;
    private PowerUpCard powerUpCardToDiscard;
    private GameBoard gameBoard;

    public SpawnAction(SpawnInfo spawnInfo, PlayerAbstract player, GameBoard gameBoard){
        this.player = player;
        this.powerUpCardToDiscard = spawnInfo.getPowerupCard();
        this.gameBoard = gameBoard;
    }

    public boolean execute(Controller controller){
        if(player.getPlayerState() != PlayerState.TOBESPAWNED && player.getPlayerState() != PlayerState.DEAD)
            return false;

        List<PowerUpCard> powerupCard = new ArrayList<>();
        powerupCard.add(powerUpCardToDiscard);
        if(!player.hasPowerUpCards(powerupCard)){
            powerUpCardToDiscard = player.getRandomPowerupCard();
        }
        player.spawn(gameBoard.getMap().getSpawnPoint(powerUpCardToDiscard.getColor())); //a spawn
        player.getHand().removePowerUpCard(powerUpCardToDiscard);

        controller.getGameManager().sendEverybodyExcept(new MessageAnswer(player.getName() +
                (player.getPlayerState()==PlayerState.TOBESPAWNED ? " spawned" : " respawned after death")), player.getClientID());

        player.setState(player.getStateAfterDeath());

        return true;
    }
}
