package client;

import server.SetRespawnAnswer;
import server.controller.turns.TurnPhase;
import server.model.game.GameState;
import server.model.map.GameMap;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;
import view.*;

import java.io.Serializable;
import java.util.*;

public class GameModel extends Observable implements Serializable {
    // so that the GUI can be an observer of this class and
    // this class is an observer of the model GAMESTATE


    private GameBoardAnswer gameBoard;
    private PlayerHandAnswer playerHand;
    private int clientID;

    private boolean toSpawn;
    private boolean toRespawn;


    private boolean disconnected;
    private boolean serverOffline;

    private boolean justDidMyTurn;

    private String message;
    private SetupRequestAnswer setupRequestAnswer;
    private GameOverAnswer gameOverAnswer;

    private boolean playTagback;

    private boolean gameOver;


    public GameModel(){ //THERE IS A NEW gamemodel for every client!
        clientID = -9;
    }

    public GameState getCurrentState(){
        return gameBoard.getResult().getGameState();
    }

    public TurnPhase getCurrentTurnPhase(){
        return gameBoard.getResult().getCurrentTurnPhase();
    }

    public void setServerOffline(boolean serverOffline) {
        this.serverOffline = serverOffline;
        setChanged();
        notifyObservers("Server offline");
    }

    public GameOverAnswer getGameOverAnswer() {
        return gameOverAnswer;
    }

    public boolean isToRespawn() {
        return toRespawn;
    }

    public void setToRespawn(boolean toRespawn) {
        this.toRespawn = toRespawn;
    }

    public boolean isPlayTagback() {
        return playTagback;
    }

    public void setPlayTagback(boolean playTagback) {
        this.playTagback = playTagback;
    }

    public boolean isServerOffline() {
        return serverOffline;
    }

    public boolean isJustDidMyTurn() {
        return justDidMyTurn;
    }

    public void setJustDidMyTurn(boolean justDidMyTurn) {
        this.justDidMyTurn = justDidMyTurn;
    }

    public void setToSpawn(boolean decision){
        toSpawn = decision;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setClientID(int clientID){
        this.clientID = clientID;
    }

    public Integer getClientID(){
        return clientID;
    }

    public SetupRequestAnswer getSetupRequestAnswer() {
        return setupRequestAnswer;
    }

    public void saveAnswer(ServerAnswer answer) {

        //the GameModel will save the answer of the Server
        // updating the model elements needed and notifying the observers
        if(answer instanceof SetupRequestAnswer){
            setupRequestAnswer = (SetupRequestAnswer) answer;
            clientID = setupRequestAnswer.getClientID();
            setChanged();
            notifyObservers("setup");
        }

        else if(answer instanceof SetupConfirmAnswer){
            toSpawn = ((SetupConfirmAnswer) answer).isSpawn();
            toRespawn = ((SetupConfirmAnswer) answer).isRespawn();
            setChanged();
            notifyObservers("setupConfirm");
        }

        else if (answer instanceof GameBoardAnswer) {
            gameBoard = (GameBoardAnswer) answer;
            setChanged();
            notifyObservers("GameBoard");
        }

        else if (answer instanceof InitialMapAnswer) {
            setChanged();
            notifyObservers("Initialized Map");
        }

        else if (answer instanceof SetSpawnAnswer) {
            setToSpawn(((SetSpawnAnswer) answer).getResult());
            setChanged();
            notifyObservers("Spawn");
        }

        else if(answer instanceof SetRespawnAnswer){
            toRespawn = true;
            setChanged();
            notifyObservers("Respawn");
        }

        else if (answer instanceof GrenadeAnswer) {
            playTagback = true;
            setChanged();
            notifyObservers("Grenade");
        }

        else if(answer instanceof NoMoreTagbacksAnswer){
            playTagback = false;
            setChanged();
            notifyObservers("No More Tagbacks");
        }

        else if (answer instanceof PlayerHandAnswer) {
            playerHand = (PlayerHandAnswer) answer;
            setChanged();
            notifyObservers("PlayerHand");
        }

        else if(answer instanceof  ChangeCurrentPlayerAnswer) {
            setChanged();
            notifyObservers("Change player");
        }

        else if(answer instanceof MessageAnswer){
            message = ((MessageAnswer) answer).getMessage();
            setChanged();
            notifyObservers("Message");
        }
        else if(answer instanceof DisconnectAnswer){
            disconnected = true;
            setChanged();
            notifyObservers("Disconnected");
        }
        else if(answer instanceof ReconnectAnswer){
            disconnected = false;
            setChanged();
            notifyObservers("Reconnected");
        }
        else if(answer instanceof GameOverAnswer){
            gameOverAnswer = (GameOverAnswer) answer;
            gameOver = true;
        }

        justDidMyTurn = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isToSpawn(){
        return this.toSpawn;
    }

    public String getMessage() {
        return message;
    }

    public GameMap getMap(){
        return gameBoard.getResult().getMap();
    }

    public PlayerBoardAnswer getPlayerBoard(int clientID) {
        return gameBoard.getPlayerBoard(clientID);
    }

    public PlayerHandAnswer getPlayerHand() {
        return playerHand;
    }

    public GameBoardAnswer getGameBoard() {
        return gameBoard;
    }

    public PlayerAbstract getMyPlayer(){
        for(GameCharacter gameCharacter : gameBoard.getResult().getActiveCharacters()){
            if(gameCharacter.getConcretePlayer().getClientID() == clientID)
                return gameCharacter.getConcretePlayer();
        }
        return null;
    }
}
