package client;

import server.ServerAnswer.ServerAnswer;
import server.model.gameboard.GameBoard;
import server.model.map.GameMap;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerBoard;
import server.model.player.PlayerHand;

import java.util.*;

public class GameModel extends Observable { // so that the GUI can be an observer of this class and this class is an observer of the model GAMESTATE


    private GameBoard gameBoard;
    private GameMap map;
    private PlayerBoard playerBoard;
    private PlayerHand playerHand;
    private int clientID;
    public static int lastClientID = -1;


    public static List<Integer> listOfClients = new ArrayList<Integer>();


    public GameModel(int clientID){ //THERE IS A NEW gamemodel for every client!
        listOfClients.add(clientID);
        this.clientID = clientID;
    }


    public static List<Integer> getListOfClients(){
        return listOfClients;
    }

    public static int getNextClientID(){
        lastClientID = lastClientID + 1;
        return lastClientID;
    }

    /*public void addClient(PlayerAbstract player, Integer clientID){
        this.playerToClient.put(player, clientID);
        this.clientID = clientID;
    }*/


    public Integer getClientID(){
        return clientID;
    }

    public void saveAnswer(ServerAnswer answer) { //the GameModel will save the answer of the Server updating the model elements needed and notifying the observers
        if (answer instanceof GameBoard) {
            gameBoard = (GameBoard) answer;
            setChanged();
            notifyObservers("GameBoard");
        }

        if (answer instanceof GameMap) {
            map = (GameMap) answer;
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

    /*public List<Integer> getListOfClients() {

        for(Map.Entry<PlayerAbstract, Integer> entry: playerToClient.entrySet()) {
            listOfClients.add(entry.getValue());
        }
        return listOfClients;
    }*/

    public GameMap getMap(){
        return this.map;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public PlayerHand getPlayerHand() {
        return playerHand;
    }
}
