package server;

import constants.Constants;
import server.controller.Controller;
import server.model.cards.PowerUpCard;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;
import view.MessageAnswer;

import java.util.TimerTask;

public class SpawnTimer extends TimerTask {

    private Controller controller;
    private int id;

    public SpawnTimer(Controller controller, int id){
        this.controller = controller;
        this.id = id;
    }

    public void run(){

        boolean endGame = false;

        System.out.println("Spawn timer " + id + " triggered");

        //disconnecting and
        //spawning not-spawned-yet players
        for(PlayerAbstract playerAbstract : controller.getPlayersToRespawn()){

            System.out.println("Disconnecting " + playerAbstract.getName() + " for inactivity");

            controller.getGameManager().disconnect(playerAbstract);
            //sending message to players
            controller.getGameManager().sendEverybodyExcept(
                    new MessageAnswer(playerAbstract.getName() + " is AFK"), playerAbstract.getClientID());

            if (controller.getGameManager().getActivePlayersNum() < Constants.MIN_PLAYERS_TO_CONTINUE) {
                endGame = true;
                break;
            }

            PowerUpCard powerUpCard = playerAbstract.getRandomPowerupCard();
            playerAbstract.spawn(controller.getCurrentGame().getCurrentGameBoard().getMap().getSpawnPoint(powerUpCard.getColor()));
            playerAbstract.getHand().removePowerUpCard(powerUpCard);
            playerAbstract.setState(PlayerState.NORMAL);

            //sending message
            controller.getGameManager().sendEverybodyExcept(
                    new MessageAnswer(playerAbstract.getName() + " respawned after death"), playerAbstract.getClientID());
        }

        if(endGame)
            controller.getGameManager().endGame();
        else
            //current phase is SPAWN_PHASE
            controller.getCurrentGame().getTurnHandler().nextPhase();
    }
}
