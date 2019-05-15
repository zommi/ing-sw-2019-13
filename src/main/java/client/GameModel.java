package client;

import server.ServerAnswer.ServerAnswer;
import server.model.gameboard.GameBoard;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerBoard;
import server.model.player.PlayerHand;

import java.util.*;

public class GameModel extends Observable { // so that the GUI can be an observer of this class and this class is an observer of the model GAMESTATE


    private GameBoard gameBoard;
    private Map map;
    private PlayerBoard playerBoard;
    private PlayerHand playerHand;
    private int lastClientID = -1;
    private int ClientID;

    private Map<PlayerAbstract, Integer> playerToClient;

    public GameModel(int ClientID){
        this.playerToClient = new HashMap<>();
        this.ClientID = ClientID;
    }


    public void addClient(PlayerAbstract player, Integer clientID){
        this.playerToClient.put(player, clientID);
        lastClientID = clientID;
    }

    public Integer getClientID(){
        return ClientID;
    }

    public void saveAnswer(ServerAnswer answer) { //the GameModel will save the answer of the Server updating the model elements needed and notifying the observers
        if (answer instanceof GameBoard) {
            gameBoard = (GameBoard) answer;
            setChanged();
            notifyObservers("GameBoard");
        }

        if (answer instanceof Map) {
            map = (Map) answer;
            setChanged();
            notifyObservers("Map");
        }

        if (answer instanceof PlayerBoard) {
            playerBoard = (PlayerBoard) answer;
            setChanged();
            notifyObservers("PlayerBoard");
        }

        if (answer instanceof PlayerHand) {
            playerHand = (PlayerHand) playerHand;
            setChanged();
            notifyObservers("PlayerHand");
        }
    }

    public GameBoard getGameBoard(){
        return this.gameBoard;
    }

    public List<Integer> getListOfClients() {
        List<Integer> listOfClients = new ArrayList<Integer>();

        for(Map.Entry<PlayerAbstract, Integer> entry: playerToClient.entrySet()) {
            listOfClients.add(entry.getValue());
        }
        return listOfClients;
    }

    public int getLastClientID() {
        return lastClientID;
    }

    public Map getMap(){
        return this.map;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public PlayerHand getPlayerHand() {
        return playerHand;
    }
}
