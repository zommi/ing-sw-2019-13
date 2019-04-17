package Player;

public class Turn {
    private PlayerAbstract currentPlayer;

    public void setCurrentPlayer(PlayerAbstract currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public PlayerAbstract getCurrentPlayer() {
        return currentPlayer;
    }

    public void playAction(Action action){
        currentPlayer.setAction(action);
        currentPlayer.doAction();
    }

    public void playPowerup(){
        currentPlayer.
    }

    public void playTurn(){

    }
}
