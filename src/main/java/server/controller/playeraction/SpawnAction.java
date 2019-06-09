package server.controller.playeraction;
import client.SpawnInfo;
import server.controller.Controller;
import server.model.cards.PowerUpCard;
import server.model.gameboard.GameBoard;
import server.model.player.PlayerAbstract;

public class SpawnAction implements Action {

    PlayerAbstract player;
    PowerUpCard powerUpCardToDiscard;
    GameBoard gameBoard;

    public SpawnAction(SpawnInfo spawnInfo, PlayerAbstract player, GameBoard gameBoard){
        this.player = player;
        this.powerUpCardToDiscard = spawnInfo.getPowerupCard();
        this.gameBoard = gameBoard;
    }

    public boolean execute(Controller controller){
        player.spawn(gameBoard.getMap().getSpawnPoint(powerUpCardToDiscard.getColor())); //a spawn
        player.getHand().removePowerUpCard(powerUpCardToDiscard);
        return true;
    }
}
