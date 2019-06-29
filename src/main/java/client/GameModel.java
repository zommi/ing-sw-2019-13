package client;

import server.controller.PlayerDiedAnswer;
import server.controller.SpawnCommandAnswer;
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
    private ListOfWeaponsAnswer weaponList;
    private boolean toSpawn;
    private boolean clientChoice;

    private boolean disconnected;
    private boolean serverOffline;

    private boolean justDidMyTurn;

    private String message;
    private SetupRequestAnswer setupRequestAnswer;
    private SetupConfirmAnswer setupConfirmAnswer;

    private boolean playTagback;


    public GameModel(){ //THERE IS A NEW gamemodel for every client!
        setupRequestAnswer = null;
        setupConfirmAnswer = null;
        clientID = -9;
    }

    public void setServerOffline(boolean serverOffline) {
        this.serverOffline = serverOffline;
        setChanged();
        notifyObservers("Server offline");
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

    public List<String> getCharactersNames(){
        return gameBoard.getCharacterNames();
    }

    public List<String> getPlayersNames(){
        return gameBoard.getPlayerNames();
    }

    public void setClientID(int clientID){
        this.clientID = clientID;
    }

    public boolean getClientChoice(){
        return this.clientChoice;
    }

    public void setClientChoice(boolean choice){
        this.clientChoice = choice;
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
            setupConfirmAnswer = (SetupConfirmAnswer) answer;
            toSpawn = ((SetupConfirmAnswer) answer).isSpawn();
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

        else if(answer instanceof ListOfWeaponsAnswer) {
            weaponList = (ListOfWeaponsAnswer) answer;
            setChanged();
            notifyObservers("Weapons list");
        }

        else if(answer instanceof PlayerDiedAnswer) {
            setChanged();
            notifyObservers("Player died");
        }

        else if(answer instanceof SpawnCommandAnswer) {
            toSpawn = true;
            setChanged();
            notifyObservers("Spawn phase");
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

        justDidMyTurn = false;
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

    public void setGrenadeAction(List<Info> infoList) {

    }

    public int getNumberOfGrenades() {
        return 0;
    }

    public List<Info> getGrenadeAction() {
        return null;
    }
}
