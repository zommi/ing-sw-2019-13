package server;

import client.ReceiverInterface;
import server.controller.Controller;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final Registry registry;
    private static final String REGISTRATION_ROOM_NAME = "gameproxy";
    private int mapChoice;
    private Controller controller;
    private int initialSkulls;
    private int clientIDadded;
    private static List<Integer> listOfClients = new ArrayList<Integer>();


    public int addClient(ReceiverInterface client){
        if(listOfClients.size() == 0){
            listOfClients = new ArrayList<>();
            listOfClients.add(0);
            this.clientIDadded = 0;
        }
        else{ //it is not the first element added in the list so it is not the first client.
            listOfClients.add(clientIDadded +1);
            this.clientIDadded = clientIDadded + 1;
        }
        System.out.println("Added the clientID ");
        return clientIDadded;
    }

    public int getClientIDadded(){
        return this.clientIDadded;
    }

    public Server() throws RemoteException{
        this.registry = LocateRegistry.createRegistry(1099);
    }

    public Controller getController(){
        return this.controller;
    }

    public void setInitialSkulls(int initialSkulls){
        this.initialSkulls = initialSkulls;
    }

    public void setMap(int numMap){
        this.mapChoice = numMap;
        this.setController(numMap, initialSkulls);
    }

    public void setController(int numMap, int initialSkulls){
        controller = new Controller(numMap, initialSkulls);
        System.out.println("Controller created");
    }

    public Registry getRegistry(){
        return this.registry;
    }

    public String getName(){  //RETURNS the name of the remote object
        return REGISTRATION_ROOM_NAME;
    }

    public void startRMI(){ //creates a new thread with the RMI connection
        ExecutorService executorRMI = Executors.newCachedThreadPool();
        executorRMI.submit(new ServerRMI(this));
    }


    public static void main(String[] args) throws RemoteException{
        Server server = new Server();
        server.startRMI();
    }
}
