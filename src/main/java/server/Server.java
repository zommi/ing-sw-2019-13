package server;

import client.Connection;
import client.ConnectionRMI;
import client.ReceiverInterface;
import server.controller.Controller;
import server.model.game.Game;
import view.InitialMapAnswer;
import view.ServerAnswer;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

    private final Registry registry;
    private Game game;
    private static final String REGISTRATION_ROOM_NAME = "gameproxy";
    private int mapChoice;
    private Controller controller;
    private int initialSkulls;
    private int clientIDadded;
    private static List<Integer> listOfClients = new ArrayList<Integer>();
    public List<Connection> listConnections = new ArrayList<>();


    public void addConnection(Connection connection){
        System.out.println("Adding the GameModel of the new client to the list");
        listConnections.add(connection);
    }



    public int startTimer(){
        try{
            TimeUnit.SECONDS.sleep(30);
        }
        catch(InterruptedException e)
        {
            System.out.println("Exception thrown");
        }
        System.out.println("I waited 30 seconds");

        if(listOfClients.size() < 3){ //if after 30 seconds we have less than 3 players, the game does not start
            System.out.println("The game still has less than 3 players");
            listConnections = null;
            listOfClients = null;
            game = null;
            return 2;
        }
        //now we have to start the game!
        else{
            game = new Game(mapChoice, initialSkulls);
            //does it work with socket too? we have to test the clienID with socket too.
            ServerAnswer mapAnswer = new InitialMapAnswer(mapChoice);
            for(int i = 0; i < listConnections.size(); i++){
                if(listConnections.get(i) instanceof ConnectionRMI){       //this methods is used to update all the RMI gameModels
                    ((ConnectionRMI) listConnections.get(i)).publishMessage(mapAnswer);
                }
            }
            return 1;
        }
    }


    public int addClient(ReceiverInterface client){
        if(listOfClients.size() == 0){
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
