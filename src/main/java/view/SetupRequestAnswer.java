package view;

public class SetupRequestAnswer implements ServerAnswer {
    private boolean gameCharacter;
    private boolean firstPlayer;

    private int clientID;

    public int getClientID() {
        return clientID;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public boolean isGameCharacter() {
        return gameCharacter;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void setGameCharacter(boolean gameCharacter) {
        this.gameCharacter = gameCharacter;
    }
}

