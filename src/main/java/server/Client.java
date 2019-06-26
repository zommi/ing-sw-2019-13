package server;

import client.ReceiverInterface;
import server.model.player.PlayerAbstract;
import view.PingAnswer;
import view.ServerAnswer;

import java.io.IOException;
import java.rmi.RemoteException;

public class Client {
    private int clientID;
    private SocketClientHandler socketClientHandler;
    private ReceiverInterface receiverInterface;
    private GameManager gameManager;
    private boolean playerSetupComplete;
    private String name;
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

    public ReceiverInterface getReceiverInterface() {
        return receiverInterface;
    }

    public SocketClientHandler getSocketClientHandler() {
        return socketClientHandler;
    }

    public void setReceiverInterface(ReceiverInterface receiverInterface) {
        this.receiverInterface = receiverInterface;
    }

    public void setSocketClientHandler(SocketClientHandler socketClientHandler) {
        this.socketClientHandler = socketClientHandler;
    }

    public boolean isConnected(){
        if(socketClientHandler == null && receiverInterface == null)
            return false;
        if(socketClientHandler != null){
            return false;
        }
        else{
            try{
                receiverInterface.publishMessage(new PingAnswer());
                return true;
            }catch(RemoteException e){
                disconnect();
                return false;
            }
        }

    }

    public void disconnect(){
        if(socketClientHandler != null) {
            socketClientHandler.setKeepThreadAlive(false);
            //socketClientHandler.getInputStream().
        }


        socketClientHandler = null;
        receiverInterface = null;
        try{
            gameManager.getController().getCurrentGame().getPlayerFromId(clientID).setConnected(false);
        }catch(NullPointerException e){
            //do nothing
        }
    }

    public void send(ServerAnswer serverAnswer){
        try {
            if (socketClientHandler != null)
                socketClientHandler.publishSocketMessage(serverAnswer);
            else if(receiverInterface != null)
                receiverInterface.publishMessage(serverAnswer);

        }catch(RemoteException e){
            disconnect();
        }

    }
}
