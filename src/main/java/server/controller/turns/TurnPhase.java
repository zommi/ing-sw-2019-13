package server.controller.turns;

public enum TurnPhase {
    FIRST_ACTION (1), SECOND_ACTION (2), END_TURN (3);
    int turn;

    TurnPhase(int turn){
        this.turn = turn;
    }

    public static TurnPhase getValue(int id){
        switch(id) {
            case 1 : return FIRST_ACTION;
            case 2 : return SECOND_ACTION;
            case 3 : return END_TURN;
            default: return null;
        }
    }

    public int getTurn(){
        return this.turn;
    }

}
