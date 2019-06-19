package server;

import client.ReceiverInterface;
import view.ServerAnswer;

import java.rmi.RemoteException;

public class Client {
    private int clientID;
    private SocketClientHandler socketClientHandler;
    private ReceiverInterface receiverInterface;

    public Client(int clientID){
        this.clientID = clientID;
    }

    public int getClientID() {
        return clientID;
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

    public void send(ServerAnswer serverAnswer){
        if(socketClientHandler != null)
            socketClientHandler.publishSocketMessage(serverAnswer);
        else {
            try {
                receiverInterface.publishMessage(serverAnswer);
            } catch (RemoteException e) {
                //disconnects client todo
            }
        }
    }
}
