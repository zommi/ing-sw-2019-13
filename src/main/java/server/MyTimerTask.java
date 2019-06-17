package server;

import java.util.Date;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    private Server server;

    public MyTimerTask(Server server){
        this.server = server;
    }

    public void run(){
        System.out.println("Timer task started at: "+new Date());
        start();
        System.out.println("Timer task finished at: "+new Date());
        //TODO ADD CHECK IF THEY ARE LESS THAN 3
        server.startMatch();
    }

    private void start() {
        try {
            //assuming it takes 20 secs to complete the task
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
