package Player;

public class Turn {
    private PlayerAbstract currentPlayer;

    public void setCurrentPlayer(PlayerAbstract currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public PlayerAbstract getCurrentPlayer() {
        return currentPlayer;
    }

    public void playFirstAction(ActionInfo info, Action firstAction){
        currentPlayer.setAction(firstAction);
        currentPlayer.doAction();
    }
}
