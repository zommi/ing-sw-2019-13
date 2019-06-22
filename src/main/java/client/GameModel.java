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
    public boolean toSpawn;
    private boolean clientChoice;
    private List<Info> grenadeAction = new ArrayList<>();
    private int numberOfGrenades;

    private String message;
    private SetupRequestAnswer setupRequestAnswer;
    private SetupConfirmAnswer setupConfirmAnswer;


    public GameModel(){ //THERE IS A NEW gamemodel for every client!
        setupRequestAnswer = null;
        setupConfirmAnswer = null;
        clientID = -9;
    }

    public void setGrenadeAction(List<Info> action){
        this.grenadeAction = action;
    }

    public List<Info> getGrenadeAction(){
        return this.grenadeAction;
    }

    public int getNumberOfGrenades(){
        return this.numberOfGrenades;
    }

    public void setToSpawn(boolean decision){
        toSpawn = decision;
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

        if(answer instanceof SetupConfirmAnswer){
            setupConfirmAnswer = (SetupConfirmAnswer) answer;
            toSpawn = ((SetupConfirmAnswer) answer).isSpawn();
            setChanged();
            notifyObservers("setupConfirm");
        }

        else if(answer instanceof ResetAnswer) {
            clientChoice = false;
            setChanged();
            notifyObservers("reset");
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
            setChanged();
            notifyObservers("Spawn phase");
        }
        else if(answer instanceof MessageAnswer){
            message = ((MessageAnswer) answer).getMessage();
            setChanged();
            notifyObservers("Message");
        }
    }

    public boolean getToSpawn(){
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

    public ListOfWeaponsAnswer getWeaponList() {return weaponList; }

    public PlayerAbstract getMyPlayer(){
        for(GameCharacter gameCharacter : gameBoard.getResult().getActiveCharacters()){
            if(gameCharacter.getConcretePlayer().getClientID() == clientID)
                return gameCharacter.getConcretePlayer();
        }
        return null;
    }

}
