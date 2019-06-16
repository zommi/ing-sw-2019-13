package server;

import view.*;

import java.io.Serializable;

public class SocketInfo implements Serializable {
    private int clientID;

    private int currentID;
    private int grenadeID;
    private String currentCharacter;

    private int mapNum;
    private int initialSkulls;
    private String serverMessage;
    private ServerAnswer serverAnswer;
    private int startGame;


    public SocketInfo(){
        this.clientID = -1;
        this.currentID = -1;
        this.grenadeID = -2;
        this.mapNum = -1;
        this.initialSkulls = -1;
        this.startGame = -1;
        currentCharacter = null;
    }


    public void setCurrentCharacter(String currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    public String getCurrentCharacter() {
        return currentCharacter;
    }

    public void setServerAnswer(ServerAnswer serverAnswer) {
        this.serverAnswer = serverAnswer;
    }

    public int getStartGame() {
        return startGame;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public void setStartGame(int startGame) {
        this.startGame = startGame;
    }

    public int getInitialSkulls() {
        return initialSkulls;
    }

    public void setInitialSkulls(int initialSkulls) {
        this.initialSkulls = initialSkulls;
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setGrenadeID(int grenadeID) {
        this.grenadeID = grenadeID;
    }

    public int getClientID() {
        return clientID;
    }

    public int getCurrentID() {
        return currentID;
    }

    public int getGrenadeID() {
        return grenadeID;
    }

    public ServerAnswer getServerAnswer() {
        return serverAnswer;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public void setMapNum(int mapNum) {
        this.mapNum = mapNum;
    }

    public int getMapNum() {
        return mapNum;
    }
}

