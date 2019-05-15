package server;

import client.ReceiverInterface;
import server.ServerAnswer.ServerAnswer;
import server.controller.Controller;
import server.controller.playeraction.Action;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameProxy extends Publisher implements GameProxyInterface {

    private ReceiverInterface clientRMI;
    private int numMap;
    private Controller controller;


    public Controller getController() {
        return controller;
    }

    protected GameProxy(ReceiverInterface client) throws RemoteException {
        UnicastRemoteObject.exportObject(this, 1099);
        this.clientRMI = client;
    }
    @Override
    public boolean makeAction(int clientID, Action action){
        return true;
    }

    @Override
    public void sendMessage(ServerAnswer message){
    }

    @Override
    public GameProxyInterface register(ReceiverInterface client){
        return null;  //TODO register aggiunge il clientID e il player abbinati nella map.
    }


    @Override
    public boolean sendPlayerName(String name){
        System.out.println("Name received");
        return true;
    }

    @Override
    public boolean sendMap(int numMap){
        System.out.println("Map choice received");
        this.numMap = numMap;
        controller = new Controller(numMap, 8);
        return true;
    }


}

