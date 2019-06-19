package server;

import client.ReceiverInterface;
import view.ServerAnswer;

import java.rmi.RemoteException;

public class Client {
    private int clientID;
    private SocketClientHandler socketClientHandler;
    private ReceiverInterface receiverInterface;
    private GameManager gameManager;

    public Client(int clientID, GameManager gameManager){
        this.clientID = clientID;
        socketClientHandler = null;
        receiverInterface = null;
        this.gameManager = gameManager;
    }

    public int getClientID() {
        return clientID;
    }

    public GameManager getGameManager() {
        return gameManager;
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

    public void disconnect(){
        socketClientHandler = null;
        receiverInterface = null;
    }

    public void send(ServerAnswer serverAnswer) throws RemoteException{
        if(socketClientHandler != null)
            socketClientHandler.publishSocketMessage(serverAnswer);
        else
            receiverInterface.publishMessage(serverAnswer);
    }
}
