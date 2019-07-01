package server;

import view.*;

import java.io.Serializable;

public class SocketInfo implements Serializable {
    private int clientID;

    private int currentID;
    private String currentCharacter;

    private ServerAnswer serverAnswer;
    private int startGame;


    public SocketInfo(){
        this.clientID = -1;
        this.currentID = -1;
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

    public void setStartGame(int startGame) {
        this.startGame = startGame;
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getClientID() {
        return clientID;
    }

    public int getCurrentID() {
        return currentID;
    }

    public ServerAnswer getServerAnswer() {
        return serverAnswer;
    }
}

