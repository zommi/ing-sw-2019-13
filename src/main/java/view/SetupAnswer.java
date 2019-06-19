package view;

public class SetupAnswer implements ServerAnswer {
    private boolean gameCharacter;
    private boolean firstPlayer;

    private int clientID;
    private int skullNum;
    private int mapNum;

    public int getClientID() {
        return clientID;
    }

    public int getMapNum() {
        return mapNum;
    }

    public int getSkullNum() {
        return skullNum;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public boolean isGameCharacter() {
        return gameCharacter;
    }

    public void setMapNum(int mapNum) {
        this.mapNum = mapNum;
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

    public void setSkullNum(int skullNum) {
        this.skullNum = skullNum;
    }
}

