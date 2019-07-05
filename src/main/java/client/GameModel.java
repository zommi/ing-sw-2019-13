package client;

import answers.SetRespawnAnswer;
import server.controller.TurnPhase;
import server.model.game.GameState;
import server.model.map.GameMap;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;
import answers.*;

import java.io.Serializable;
import java.util.*;

/**
 * The gamemodel class is what allows the client to interact with the server,
 * this is a light version of the model and it contains all the information
 * a player can read at any given point. It implements the observable interface
 * because both GUI and CLI are notified whenever there is a change on the gamemodel.
 *
 * The gamemodel is updated updated by the server thanks to specific Answers.
 */
public class GameModel extends Observable implements Serializable {

    /**
     * Reference to the game board sent by the server.
     */
    private GameBoardAnswer gameBoard;

    /**
     * Reference of a player's own hand.
     */
    private PlayerHandAnswer playerHand;

    /**
     * Id of the client on the server.
     */
    private int clientID;

    /**
     * Boolean set by the server when the player needs to spawn
     * at the start of the game.
     */
    private boolean toSpawn;

    /**
     * Boolean value set by the server when a player dies and he
     * has to respawn.
     */
    private boolean toRespawn;

    /**
     * Boolean value used for soft disconnection due to inactivity
     */
    private boolean disconnected;

    /**
     * Boolean value set when the server goes offline
     */
    private boolean serverOffline;

    private boolean gamemodelNotUpdated;

    /**
     * Last message received by the client from the server.
     */
    private String message;

    /**
     * Message that asks the player information regarding the information
     * needed for setup.
     */
    private SetupRequestAnswer setupRequestAnswer;

    /**
     * Message that notifies the player of the end of the game and that contains
     * the information regarding the winner.
     */
    private GameOverAnswer gameOverAnswer;

    /**
     * Boolean that is set to true when the player can play a tagback grenade.
     */
    private boolean playTagback;

    /**
     * Boolean set to true when the client receives a GameOverAnswer.
     */
    private boolean gameOver;


    public GameModel(){
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

    /**
     * The method is used to see if the player has to be respawned
     * @return boolean that indicates if the player has to be respawned
     */
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

    public boolean isGamemodelNotUpdated() {
        return gamemodelNotUpdated;
    }

    public void setGamemodelNotUpdated(boolean gamemodelNotUpdated) {
        this.gamemodelNotUpdated = gamemodelNotUpdated;
    }

    public void setToSpawn(boolean decision){
        toSpawn = decision;
    }

    /**
     * The method is used to see if the player has been disconnected
     * @return boolean that indicates if the player has been disconnected
     */
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

    /**
     * The method is used to save the answers that the connection gets from the server, and notifies the observers
     */
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
            serverOffline = ((SetupConfirmAnswer) answer).isServerOffline();
            setChanged();
            notifyObservers("setupConfirm");
        }

        else if (answer instanceof GameBoardAnswer) {
            gameBoard = (GameBoardAnswer) answer;
            setChanged();
            notifyObservers("GameBoard");
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
            setChanged();
            notifyObservers("Game Over");
        }

        gamemodelNotUpdated = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * The method is used to see if the player has to be spawned
     * @return boolean that indicates if the player has to be spawned
     */
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
