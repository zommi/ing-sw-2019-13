package server;

import java.util.Date;
import java.util.TimerTask;

public class GameStartTimer extends TimerTask {

    private GameManager gameManager;

    public GameStartTimer(GameManager gameManager){
        this.gameManager = gameManager;
    }

    public void run(){
        System.out.println("GameStartTimer triggered at: " + new Date() + "\nStarting match right now");

        //creating new GameManager in server
        gameManager.getServer().setCurrentGameManager(new GameManager(gameManager.getServer()));

        //start the old GameManager
        gameManager.startMatch();
    }
}
