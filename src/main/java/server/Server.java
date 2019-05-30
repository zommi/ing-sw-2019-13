package server;

import client.ReceiverInterface;
import server.controller.Controller;
import server.model.game.Game;
import view.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

    private final Registry registry;
    private GameProxyInterface gameProxy;
    private Game game;
    private static final String REGISTRATION_ROOM_NAME = "gameproxy";
    private int mapChoice;
    private Controller controller;
    private int initialSkulls;
    private int clientIDadded;
    private int startGame = 0;
    private static List<Integer> listOfClients = new ArrayList<Integer>();

    public int getStartGame(){
        return this.startGame;
    }

    public int startMatch(){ //TODO here we have a problem: what if the player does not choose the character in time?
        if(listOfClients.size() < 3){ //if after 30 seconds we have less than 3 players, the game does not start
            System.out.println("The game still has less than 3 players");
            listOfClients = null;
            game = null;
            startGame = 2;
            return 2;
        }
        //now we have to start the game!
        else{
            game = controller.getCurrentGame();
            System.out.println("Created the game");
            //does it work with socket too? we have to test the clienID with socket too.
            ServerAnswer mapAnswer = new MapAnswer(this.game.getCurrentGameMap());
            System.out.println("Now I will send the map to the client");
            try{ //TODO WITH SOCKET CONNECTION!!!!!
                InitialMapAnswer temp0 = new InitialMapAnswer(mapChoice);
                List<ReceiverInterface> temp = gameProxy.getClientRMIadded();
                ListOfWeaponsAnswer temp1 = controller.getCurrentGame().getWeaponList(); //piazzare una lista di socket e aggiornarla
                GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(controller.getCurrentGame().getCurrentGameBoard());
                for(int i = 0; i < temp.size(); i++){
                    System.out.println("Found a connection whose client is: " + temp.get(i).getClientID());
                    temp.get(i).publishMessage(temp0);
                    temp.get(i).publishMessage(gameBoardAnswer);
                    temp.get(i).publishMessage(mapAnswer);
                    System.out.println("Sent the map to the connection RMI");
                    temp.get(i).publishMessage(temp1);
                    System.out.println("Sent the weapon card list to the client RMI");
                    System.out.println(" " +temp.get(i).getClientID());
                    System.out.println(" " +controller.getCurrentID());
                    if(temp.get(i).getClientID() == controller.getCurrentID()){
                        System.out.println("Trying to find the current player to send him his player hand");
                        for(int k = 0; k < controller.getPlayers().size(); k++){
                            System.out.println(" " +controller.getPlayers().get(k).getClientID());
                            if(controller.getPlayers().get(k).getClientID() == controller.getCurrentID()){
                                System.out.println("Found him. Now I will send him his player hand");
                                PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(controller.getPlayers().get(k).getHand());
                                temp.get(i).publishMessage(playerHandAnswer);
                                System.out.println("player hand sent");
                            }
                        }
                    }
                }
            }
            catch(RemoteException e){
                System.out.println("Exception caught");
                e.printStackTrace();
            }

            startGame = 1;
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

    public void sendToEverybodyRMI(ServerAnswer serverAnswer) {
        try {
            List<ReceiverInterface> temp = gameProxy.getClientRMIadded();
            for (int i = 0; i < temp.size(); i++) {
                temp.get(i).publishMessage(serverAnswer);
                System.out.println("Sent an update to the client");
            }
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
    }

    public void sendToSpecificRMI(ServerAnswer serverAnswer, int clientID){
        try {
            List<ReceiverInterface> temp = gameProxy.getClientRMIadded();
            for (int i = 0; i < temp.size(); i++) {
                if(temp.get(i).getClientID() == clientID){
                    temp.get(i).publishMessage(serverAnswer);
                    System.out.println("Sent an update to the client");
                    break;
                }
            }
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
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
        System.out.println("Instantiating the controller");
        this.setController(numMap, initialSkulls);
    }

    public void setController(int numMap, int initialSkulls){
        //System.out.println("Test");
        controller = new Controller(numMap, initialSkulls, this);
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
        Server rmiServer = new Server();
        SocketServer socketServer = new SocketServer(1337);
        rmiServer.startRMI();
        socketServer.start();
    }


}
