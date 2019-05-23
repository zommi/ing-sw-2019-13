package server;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final Registry registry;
    private GameProxyInterface gameProxy;
    private Game game;
    private static final String REGISTRATION_ROOM_NAME = "gameproxy";
    private int mapChoice;
    private Controller controller;
    private int initialSkulls;
    private int clientIDadded;
    private boolean startGame = false;
    private static List<Integer> listOfClients = new ArrayList<Integer>();

    public boolean getStartGame(){
        return this.startGame;
    }

    public int startMatch(){
        if(listOfClients.size() < 3){ //if after 30 seconds we have less than 3 players, the game does not start
            System.out.println("The game still has less than 3 players");
            listOfClients = null;
            game = null;
            return 2;
        }
        //now we have to start the game!
        else{
            startGame = true;
            game = new Game(mapChoice, initialSkulls);
            System.out.println("Created the game");
            //does it work with socket too? we have to test the clienID with socket too.
            ServerAnswer mapAnswer = new InitialMapAnswer(mapChoice);
            System.out.println("Now I will send the map to the client");
            try{ //TODO WITH SOCKET CONNECTION!!!!!
                List<ReceiverInterface> temp = gameProxy.getClientRMIadded();
                for(int i = 0; i < temp.size(); i++){
                    System.out.println("Found a connection whose client is: " + temp.get(i).getClientID());
                    temp.get(i).publishMessage(mapAnswer);
                    System.out.println("Sent the map to the connection RMI");
                }
            }
            catch(RemoteException e){
                System.out.println("Exception caught");
                e.printStackTrace();
            }

            System.out.println("The game is starting");

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

        if(listOfClients.size() == 3){ //start timer di N secondi
            TimerTask timerTask = new MyTimerTask(this);
            Timer timer = new Timer(true);
            timer.schedule(timerTask, 0);
            System.out.println("Task started");
        }
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

    public void setGameProxy(GameProxyInterface gameProxy){
        this.gameProxy = gameProxy;
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
