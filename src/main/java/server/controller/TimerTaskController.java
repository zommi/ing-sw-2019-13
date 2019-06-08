package server.controller;


import java.util.Date;
import java.util.TimerTask;

public class TimerTaskController extends TimerTask{

        public void run(){
            System.out.println("Timer task started at:"+new Date());
            start();
            System.out.println("Timer task finished at:"+new Date());
        }

        private void start() {
            try {
                //assuming it takes 20 secs to complete the task
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
}
