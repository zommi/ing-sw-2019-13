package server;

import client.ReceiverInterface;
import exceptions.WrongGameStateException;
import server.controller.Controller;
import server.model.game.Game;
import server.model.gameboard.GameBoard;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerHand;
import server.model.player.PlayerState;
import view.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final Registry registry;
    private GameProxyInterface gameProxy;
    private Game game;
    private static final String REGISTRATION_ROOM_NAME = "gameproxy";
    private int mapChoice;
    private Controller controller;
    private int idToDisconnect = -1;
    private int initialSkulls;
    private int clientIDadded;
    private int startGame = 0;
    private List<Integer> listOfClients = new ArrayList<>();
    private List<PlayerAbstract> playerList = new ArrayList<>();
    private List<PlayerAbstract> playerDisconnectedList = new ArrayList<>();


    public int getStartGame(){
        return this.startGame;
    }
    public GameProxyInterface getGameProxy(){
        return gameProxy;
    }

    //posso far partire un thread del game passandogli playerList, controller, gameproxy
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
            game.setPlayersNames();

            //adding active characters to the gameboard
            for(PlayerAbstract playerAbstract : game.getActivePlayers()){
                game.getCurrentGameBoard().getActiveCharacters().add(playerAbstract.getGameCharacter());
            }

            System.out.println("Created the game");
            //does it work with socket too? we have to test the clienID with socket too.
            //ServerAnswer mapAnswer = new MapAnswer(this.game.getCurrentGameMap());
            System.out.println("Now I will send the map to the client");
            try{ //TODO WITH SOCKET CONNECTION!!!!!
                InitialMapAnswer temp0 = new InitialMapAnswer(mapChoice);
                HashMap<Integer, ReceiverInterface> temp = gameProxy.getClientRMIadded();
                //ListOfWeaponsAnswer temp1 = controller.getCurrentGame().getWeaponList(); //piazzare una lista di socket e aggiornarla
                GameBoard currentGameBoard = controller.getCurrentGame().getCurrentGameBoard();
                GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(currentGameBoard);
                SetSpawnAnswer setSpawnAnswer = new SetSpawnAnswer(true); //at the very start all of them need to be spawned
                for(Map.Entry<Integer, ReceiverInterface> entry : temp.entrySet()) {
                    ReceiverInterface value = entry.getValue();
                    System.out.println("Found a connection whose client is: " + value.getClientID());
                    value.publishMessage(temp0);
                    value.publishMessage(gameBoardAnswer);
                    //temp.get(i).publishMessage(mapAnswer);
                    value.publishMessage(setSpawnAnswer);
                    System.out.println("Sent the map to the connection RMI");
                    //temp.get(i).publishMessage(temp1);
                    //System.out.println("Sent the weapon card list to the client RMI");
                    System.out.println(" " +value.getClientID());
                    System.out.println("Sending the players their player hands");
                    for(int k = 0; k < controller.getPlayers().size(); k++){
                        if(controller.getPlayers().get(k).getClientID() == value.getClientID()){
                            PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(controller.getPlayers().get(k).getHand());
                            try{
                                value.publishMessage(playerHandAnswer);
                                System.out.println("player hand sent");
                            }
                            catch(RemoteException e){
                                e.printStackTrace();
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
            try{
                game.nextState();
            }
            catch(WrongGameStateException e){
                e.printStackTrace();
            }
            System.out.println("We are in the game state: " +game.getCurrentState());
            return 1; //not useful
        }
    }

    public void addPlayer(PlayerAbstract player){
        playerList.add(player);
        if(playerList.size() == 3){ //start timer di N secondi
            TimerTask timerTask = new MyTimerTask(this);
            Timer timer = new Timer(true);
            timer.schedule(timerTask, 0);
            System.out.println("Task started");
        }
    }

    public void addClientsDisconnected(int id){
        this.listOfClients.add(id);
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

    public void sendToEverybodyRMI(ServerAnswer serverAnswer) {
        try {
            HashMap<Integer, ReceiverInterface> temp = gameProxy.getClientRMIadded();
            for(Map.Entry<Integer, ReceiverInterface> entry : temp.entrySet()) {
                ReceiverInterface value = entry.getValue();
                idToDisconnect = entry.getKey();  //when it catches the exception we know which id is the one
                value.publishMessage(serverAnswer);
                System.out.println("Sent an update to the clients");
            }
        }
        catch(RemoteException e){
            System.out.println("A client has been disconnected");
            try{
                for(int i = 0; i < listOfClients.size(); i++){
                    if(listOfClients.get(i) == idToDisconnect){
                        listOfClients.remove(i);
                    }
                }
                if(listOfClients.size() < 3){
                    //TODO termina la partita
                    System.out.println("The game is ended. We are now proceeding in proclaiming the winner");
                }
                else
                {
                    System.out.println("Disconnecting the player: "+idToDisconnect);
                    gameProxy.getClientRMIadded().remove(idToDisconnect); //ELIMINATES THE CONNECTION FROM THE CONNECTION HASMAP
                    for(PlayerAbstract p:playerList){
                        if(p.getClientID() == idToDisconnect){   //ELIMINATES THE PLAYER FROM THE LIST IN SERVER
                            playerDisconnectedList.add(p);
                            p.setState(PlayerState.DISCONNECTED);
                            System.out.println("Disconnected the player: " +idToDisconnect);
                        }
                    }
                    game.getCurrentGameBoard().getHashMap().remove(idToDisconnect);
                }
            }
            catch(RemoteException re){
                re.printStackTrace();
            }

        }
    }

    public void sendToSpecificRMI(ServerAnswer serverAnswer, int clientID){
        try {
            HashMap<Integer, ReceiverInterface> temp = gameProxy.getClientRMIadded();
            System.out.println("Sending the update to: "+clientID);
            for (Map.Entry<Integer, ReceiverInterface> entry : temp.entrySet()) {
                ReceiverInterface value = entry.getValue();
                idToDisconnect = entry.getKey();  //when it catches the exception we know which id is the one
                if(value.getClientID() == clientID){
                    temp.get(idToDisconnect).publishMessage(serverAnswer);
                    System.out.println("Sent an update to a client");
                    break;
                }
            }
        }
        catch(RemoteException e){
            System.out.println("A client has been disconnected");
            try{
                for(int i = 0; i < listOfClients.size(); i++){
                    if(listOfClients.get(i) == idToDisconnect){
                        listOfClients.remove(i);
                    }
                }
                if(listOfClients.size() < 3){
                    //TODO termina la partita
                    System.out.println("The game is ended. We are now proceeding in proclaiming the winner");
                }
                else
                {
                    System.out.println("Disconnecting the player: " +idToDisconnect);
                    gameProxy.getClientRMIadded().remove(idToDisconnect); //ELIMINATES THE CONNECTION FROM THE CONNECTION HASMAP
                    for(PlayerAbstract p:playerList){
                        if(p.getClientID() == idToDisconnect){   //ELIMINATES THE PLAYER FROM THE LIST IN SERVER
                            playerDisconnectedList.add(p);
                            p.setState(PlayerState.DISCONNECTED);
                            System.out.println("Disconnected the player: " +idToDisconnect);
                        }
                    }
                    game.getCurrentGameBoard().getHashMap().remove(idToDisconnect);
                }
            }
            catch(RemoteException re){
                re.printStackTrace();
            }

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
