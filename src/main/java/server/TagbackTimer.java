package server;

import server.controller.Controller;
import server.controller.turns.TurnPhase;

public class TagbackTimer {
    private Controller controller;
    private int id;
    private TurnPhase lastTurnPhase;

    public TagbackTimer(Controller controller, int id, TurnPhase turnPhase){
        this.controller = controller;
        this.id = id;
        this.lastTurnPhase = turnPhase;
    }

    public void run(){
        System.out.println("Tagback timer " + id + " triggered, moving to END_TURN");


    }
}
