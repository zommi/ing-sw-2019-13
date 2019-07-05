package server;

import client.ReceiverInterface;
import server.model.player.PlayerAbstract;
import answers.MessageAnswer;
import answers.PingAnswer;
import answers.ServerAnswer;

import java.rmi.RemoteException;

/**
 * Abstraction for rmi and socket clients. This object represents a connected client, and it is a kind of
 * common interface.
 * @author Matteo Pacciani
 */

public class Client {
    /**
     * the assigned id
     */
    private int clientID;
    /**
     * Not null if this client is connected using socket
     */
    private SocketClientHandler socketClientHandler;
    /**
     * Remote object of the rmi connected client, null if it's using socket
     */
    private ReceiverInterface receiverInterface;
    /**
     * The game manager this client is assigned to
     */
    private GameManager gameManager;
    /**
     * True if the player has completed his in-game player setup
     */
    private boolean playerSetupComplete;
    /**
     * The name of the client
     */
    private String name;
    /**
     * True if this client is designed as first player of his match: he will have to decide
     * initial skulls and the map
     */
    private boolean firstPlayer;

    public Client(int clientID, GameManager gameManager, String name){
        this.clientID = clientID;
        socketClientHandler = null;
        receiverInterface = null;
        this.gameManager = gameManager;
        playerSetupComplete = false;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public int getClientID() {
        return clientID;
    }

    public boolean isPlayerSetupComplete() {
        return playerSetupComplete;
    }

    public void setPlayerSetupComplete(boolean playerSetupComplete) {
        this.playerSetupComplete = playerSetupComplete;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public PlayerAbstract getPlayer(){
        return gameManager.getController().getCurrentGame().getPlayer(name);
    }

    public void setReceiverInterface(ReceiverInterface receiverInterface) {
        this.receiverInterface = receiverInterface;
    }

    public void setSocketClientHandler(SocketClientHandler socketClientHandler) {
        this.socketClientHandler = socketClientHandler;
    }

    /**
     * Checks if the client is connected to the server
     * @return true if the client is connected, false otherwise
     */
    public boolean isConnected(){
        if(socketClientHandler == null && receiverInterface == null)
            return false;
        if(socketClientHandler != null){
            return true;
        }
        else{
            try{
                receiverInterface.publishMessage(new PingAnswer());
                return true;
            }catch(RemoteException e){
                disconnectClient();
                return false;
            }
        }

    }

    /**
     * Called when an exception is thrown, sets the client as disconnected so that it will be accepted when he
     * will reconnect again
     */
    public void disconnectClient(){
        System.out.println("Lost connection with " + name);

        socketClientHandler = null;
        receiverInterface = null;

        //if the game is not started, the client is gonna be completely removed from the server
        if(gameManager.getGameStarted() == 0 || !playerSetupComplete){
            gameManager.getServer().removeClient(clientID);
        }else if(gameManager.getGameStarted() == 1 && !gameManager.isGameOver() && playerSetupComplete){
            gameManager.sendEverybodyExcept(new MessageAnswer(name + " lost connection with the server"), clientID);
            gameManager.setInactive(getPlayer());
        }

        System.out.println(name + " has successfully been disconnected from the server");
    }

    /**
     * Sends an answer to the client using socket or rmi
     * @param serverAnswer the answer to send
     */
    public void send(ServerAnswer serverAnswer){

        if(gameManager.isGameOver())
            return;

        if (socketClientHandler != null)
            socketClientHandler.publishSocketMessage(serverAnswer);
        else if(receiverInterface != null) {

            new Thread(){

                @Override
                public void run() {
                    try {
                        receiverInterface.publishMessage(serverAnswer);
                    }catch(RemoteException e){
                        disconnectClient();
                    }

                }
            }.start();
        }
    }
}
