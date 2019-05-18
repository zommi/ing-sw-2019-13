package server;

import client.ActionInfo;
import client.ReceiverInterface;
import server.ServerAnswer.ServerAnswer;
import server.controller.playeraction.Action;
import server.model.player.ConcretePlayer;
import server.model.player.PlayerAbstract;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameProxy extends Publisher implements GameProxyInterface, Serializable {

    private ReceiverInterface clientRMI;
    private int numMap;
    private String playerName;
    private ServerRMI serverRMI;
    private PlayerAbstract player;
    private int clientIDadded;
    private int initialSkulls;


    protected GameProxy(ServerRMI serverRMI) throws RemoteException {
        this.serverRMI = serverRMI;
        UnicastRemoteObject.exportObject(this, 1099);
    }

    @Override
    public boolean makeAction(int clientID, ActionInfo action)  throws RemoteException{
        return true;
    }

    @Override
    public void sendMessage(ServerAnswer message){
    }

    @Override
    public void register(ReceiverInterface client) throws RemoteException, NotBoundException{
        System.out.println("Adding the client to the server...");
        this.clientIDadded = serverRMI.addClient(client);

        /*System.out.println("I am trying to connect to the client");
        Registry registryClient = LocateRegistry.getRegistry("localhost",1000);

        client = (ReceiverInterface) registryClient.lookup("rmiconnection");
        System.out.println("I just connected to the client");*/
    }

    @Override
    public int getClientID() throws RemoteException{
        return this.clientIDadded;
    }

    @Override
    public void setClientRMI(ReceiverInterface clientRMI) throws RemoteException{
        System.out.println("Trying to connect the server to the client");
        this.clientRMI = clientRMI;
        System.out.println("I just connected to the client");
    }

    @Override
    public boolean sendPlayer(String name)  throws RemoteException{
        System.out.println("Name received");
        this.playerName = name;
        this.player = new ConcretePlayer(name);
        return true;
    }

    @Override
    public String getMap() throws RemoteException{
        if(this.numMap == 1)
            return "map11.txt";
        else if(this.numMap == 2)
            return "map12.txt";
        else if(this.numMap == 3)
            return "map21.txt";
        else if(this.numMap == 4)
            return "map22.txt";
        else
            return "No one has chosen yet";
    }

    @Override
    public boolean sendInitialSkulls(int initialSkulls) throws RemoteException{
        this.initialSkulls = initialSkulls;
        serverRMI.getServer().setInitialSkulls(initialSkulls);
        System.out.println("Initial skulls choice received");
        return true;
    }

    @Override
    public int getInitialSkulls() throws RemoteException{
        return this.initialSkulls;
    }


    @Override
    public boolean sendMap(int numMap)  throws RemoteException{
        System.out.println("Map choice received");
        this.numMap = numMap;
        serverRMI.getServer().setMap(numMap);
        serverRMI.setController(serverRMI.getServer().getController());
        System.out.println("Controller set for serverRMI");
        serverRMI.addMapClient();
        System.out.println("Added the map of the client and of the player");
        return true;
    }

    @Override
    public PlayerAbstract getPlayer() throws RemoteException{
        return player;
    }

}

