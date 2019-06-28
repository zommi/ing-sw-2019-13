package server;

import java.util.Date;
import java.util.TimerTask;

public class GameStartTimer extends TimerTask {

    private GameManager gameManager;

    public GameStartTimer(GameManager gameManager){
        this.gameManager = gameManager;
    }

    public void run(){
        System.out.println("Timer task started at: "+new Date());
        start();
        System.out.println("Timer task finished at: "+new Date());

        //creating new GameManager in server
        gameManager.getServer().setCurrentGameManager(new GameManager(gameManager.getServer()));

        //start the old GameManager
        gameManager.startMatch();
    }

    private void start() {
        try {
            //assuming it takes 20 secs to complete the task
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}