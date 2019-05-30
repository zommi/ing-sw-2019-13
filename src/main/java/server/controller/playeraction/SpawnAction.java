package server.controller.playeraction;
import client.SpawnInfo;
import server.model.cards.PowerupCard;
import server.model.gameboard.GameBoard;
import server.model.player.PlayerAbstract;

public class SpawnAction implements Action {

    PlayerAbstract player;
    PowerupCard powerupCardToDiscard;
    GameBoard gameBoard;

    public SpawnAction(SpawnInfo spawnInfo, PlayerAbstract player, GameBoard gameBoard){
        this.player = player;
        this.powerupCardToDiscard = spawnInfo.getPowerupCard();
        this.gameBoard = gameBoard;
    }
    public boolean execute(){
        player.spawn(gameBoard.getMap().getSpawnPoint(powerupCardToDiscard.getColor())); //a spawn
        player.getHand().removePowerUpCard(powerupCardToDiscard);
        return true;
    }
}
