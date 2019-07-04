package server;

import client.ReceiverInterface;
import server.model.player.PlayerAbstract;
import view.MessageAnswer;
import view.PingAnswer;
import view.ServerAnswer;

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

    public void send(ServerAnswer serverAnswer){

        if(gameManager.isGameOver())
            return;

        try {
            if (socketClientHandler != null)
                socketClientHandler.publishSocketMessage(serverAnswer);
            else if(receiverInterface != null)
                receiverInterface.publishMessage(serverAnswer);

        }catch(RemoteException e){
            disconnectClient();
        }

    }
}
