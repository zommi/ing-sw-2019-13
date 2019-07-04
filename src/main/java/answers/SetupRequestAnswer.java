package answers;

public class SetupRequestAnswer implements ServerAnswer {
    private boolean gameCharacter;
    private boolean firstPlayer;

    private int clientID;

    private boolean reconnection;

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

    public void setReconnection(){this.reconnection = true;}

    public boolean isReconnection() {
        return reconnection;
    }
}

