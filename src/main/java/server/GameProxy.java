package server;

import client.ReceiverInterface;
import server.ServerAnswer.ServerAnswer;
import server.controller.Controller;
import server.controller.playeraction.Action;
import server.model.player.ConcretePlayer;
import server.model.player.PlayerAbstract;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class GameProxy extends Publisher implements GameProxyInterface, Serializable {

    private ReceiverInterface clientRMI;
    private int numMap;
    private String playerName;
    private Controller controller;
    private ServerRMI serverRMI;
    private PlayerAbstract player;
    private ReceiverInterface client;


    public Controller getController() {
        return controller;
    }

    protected GameProxy(ServerRMI serverRMI) throws RemoteException {
        this.serverRMI = serverRMI;
        UnicastRemoteObject.exportObject(this, 1099);
    }

    @Override
    public boolean makeAction(int clientID, Action action){
        return true;
    }

    @Override
    public void sendMessage(ServerAnswer message){
    }

    @Override
    public void register(ReceiverInterface client) throws RemoteException, NotBoundException{
        System.out.println("Adding the client to the server...");
        serverRMI.addClient(client);

        /*System.out.println("I am trying to connect to the client");
        Registry registryClient = LocateRegistry.getRegistry("localhost",1000);

        client = (ReceiverInterface) registryClient.lookup("rmiconnection");
        System.out.println("I just connected to the client");*/
    }

    @Override
    public void setClientRMI(ReceiverInterface clientRMI) throws RemoteException{
        System.out.println("Trying to connect the server to the client");
        this.clientRMI = clientRMI;
        System.out.println("I just connected to the client");
    }

    @Override
    public boolean sendPlayer(String name){
        System.out.println("Name received");
        this.playerName = name;
        this.player = new ConcretePlayer(name);
        return true;
    }

    @Override
    public boolean sendMap(int numMap){
        System.out.println("Map choice received");
        this.numMap = numMap;
        controller = new Controller(numMap, 8);
        serverRMI.setController(controller);
        serverRMI.addMapClient();
        System.out.println("Controller created");
        return true;
    }

    @Override
    public PlayerAbstract getPlayer(){
        return player;
    }

}

