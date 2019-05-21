package server;

import client.Connection;
import client.ConnectionRMI;
import client.GameModel;
import client.ReceiverInterface;
import server.controller.Controller;
import view.MapAnswer;
import view.MapElement;
import view.ServerAnswer;

import java.util.Timer;
import java.util.TimerTask;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final Registry registry;
    private static final String REGISTRATION_ROOM_NAME = "gameproxy";
    private int seconds;
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



    public boolean startTimer(){

        Timer timer = new Timer();
        seconds = 0;
        TimerTask task;
        task = new TimerTask() {
            @Override
            public void run() {
                while (seconds < 30) {
                    System.out.println("Seconds = " + seconds);
                    seconds++;
                }
            }
        };
        timer.schedule(task, 0, 30);
        //now we have to start the game!

        ArrayList<MapElement> charactersPosition = new ArrayList<>();
        controller.getPlayerToClient();
        //TODO INITIALIZE GAME!
        //TODO initialize charactersPosition. Maybe I could just eliminate the charactersPosition
        ServerAnswer mapAnswer = new MapAnswer(mapChoice, charactersPosition);
        for(int i = 0; i < listConnections.size(); i++){
            if(listConnections.get(i) instanceof ConnectionRMI){       //this methods is used to update all the RMI gameModels
                ((ConnectionRMI) listConnections.get(i)).publishMessage(mapAnswer);
            }
        }
        return true;
    }


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
