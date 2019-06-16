package server;

import client.ReceiverInterface;
import constants.Constants;
import exceptions.WrongGameStateException;
import server.controller.Controller;
import server.model.game.Game;
import server.model.gameboard.GameBoard;
import server.model.player.Figure;
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
    private int lastClientIdAdded;
    private int startGame = 0;
    private List<Integer> listOfClients = new ArrayList<>();
    private List<PlayerAbstract> playerList = new ArrayList<>();
    private List<PlayerAbstract> playerDisconnectedList = new ArrayList<>();

    private ServerRMI serverRMI;
    private SocketServer socketServer;


    public Server() throws RemoteException{
        this.registry = LocateRegistry.createRegistry(1099);
    }


    public int getStartGame(){
        return this.startGame;
    }
    public GameProxyInterface getGameProxy(){
        return gameProxy;
    }

    public int getInitialSkulls() {
        return initialSkulls;
    }

    //posso far partire un thread del game passandogli playerList, controller, gameproxy
    public int startMatch(){ //TODO here we have a problem: what if the player does not choose the character in time?
        if(listOfClients.size() < Constants.MIN_PLAYERS){ //if after 30 seconds we have less than 3 players, the game does not start
            System.out.println("The game still has less than 3 players");
            listOfClients = null;
            game = null;
            startGame = 2;

            //informs socket clients
            sendToEverybodySocket(null);

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
            System.out.println("Now I will send the map to the client");

            //prepares initial info
            InitialMapAnswer initialMapAnswer = new InitialMapAnswer(mapChoice);
            GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(controller.getCurrentGame().getCurrentGameBoard());
            SetSpawnAnswer setSpawnAnswer = new SetSpawnAnswer(true); //at the very start all of them need to be spawned

            //sends initial info with RMI
            try{
                HashMap<Integer, ReceiverInterface> clientRMIaddedMap = gameProxy.getClientRMIadded();
                for(Map.Entry<Integer, ReceiverInterface> clientRMIadded : clientRMIaddedMap.entrySet()) {
                    ReceiverInterface clientRMI = clientRMIadded.getValue();
                    System.out.println("Found a connection whose client is: " + clientRMI.getClientID());
                    clientRMI.publishMessage(initialMapAnswer);
                    clientRMI.publishMessage(gameBoardAnswer);
                    clientRMI.publishMessage(setSpawnAnswer);
                    System.out.println("Sent the map to the connection RMI");
                    System.out.println(" " + clientRMI.getClientID());
                    System.out.println("Sending the players their player hands");
                    for(int k = 0; k < controller.getPlayers().size(); k++){
                        if(controller.getPlayers().get(k).getClientID() == clientRMI.getClientID()){
                            PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(controller.getPlayers().get(k).getHand());
                            try{
                                clientRMI.publishMessage(playerHandAnswer);
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

            //sends initial info with socket
            sendToEverybodySocket(initialMapAnswer);
            sendToEverybodySocket(gameBoardAnswer);
            sendToEverybodySocket(setSpawnAnswer);
            for(SocketClientHandler socketClientHandler : socketServer.getClientsAdded()){
                PlayerHand playerHand = controller.getCurrentGame().getPlayerFromId(socketClientHandler.getClientID()).getHand();
                PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(playerHand);
                socketClientHandler.publishSocketMessage(playerHandAnswer);
            }

            startGame = 1;

            //informs socket client
            sendToEverybodySocket(null);

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
        if(playerList.size() == Constants.MIN_PLAYERS){ //start timer di N secondi
            TimerTask timerTask = new MyTimerTask(this);
            Timer timer = new Timer(true);
            timer.schedule(timerTask, 0);
            System.out.println("Task started");
        }
    }

    public void addClientsDisconnected(int id){
        this.listOfClients.add(id);
    }

    public Figure getFreeFigure(){

        //assuming maximum number of player has not been reached

        for(Figure figure : Figure.values()){
            if(!isCharacterTaken(figure.name()))
                return figure;
        }
        return null; //this should never happen
    }

    public boolean isCharacterTaken(String nameChar){
        System.out.println("Checking if the character is already taken by someone else");
        System.out.println("In my list I have " +playerList.size() +"players, i will check if they already have chosen their characters");
        for(int i = 0; i < playerList.size(); i++){
            if((playerList.get(i).getIfCharacter() == true)&&(playerList.get(i).getCharacterName().equalsIgnoreCase(nameChar))){
                System.out.println("Found " +playerList.get(i).getCharacterName());
                System.out.println("The character is already taken by someone else");
                return true;
            }
        }
        System.out.println("The character name you chose is ok");
        return false;
    }


    public int addClient(ReceiverInterface client){
        if(listOfClients.isEmpty()){
            listOfClients.add(0);
            this.lastClientIdAdded = 0;
        }
        else{ //it is not the first element added in the list so it is not the first client.
            listOfClients.add(lastClientIdAdded +1);
            this.lastClientIdAdded = lastClientIdAdded + 1;
        }
        System.out.println("Added the clientID ");
        return lastClientIdAdded;
    }

    public int addClient(SocketClientHandler client){
        if(listOfClients.isEmpty()){
            listOfClients.add(0);
            this.lastClientIdAdded = 0;
        }
        else{ //it is not the first element added in the list so it is not the first client.
            listOfClients.add(lastClientIdAdded +1);
            this.lastClientIdAdded = lastClientIdAdded + 1;
        }
        System.out.println("Added the clientID ");
        return lastClientIdAdded;
    }

    private SocketClientHandler getSocketClientFromID(int clientID){
        for(SocketClientHandler socketClientHandler : socketServer.getClientsAdded()){
            if(socketClientHandler.getClientID() == clientID)
                return socketClientHandler;
        }

        return null;
    }

    public void sendToSpecific(ServerAnswer serverAnswer, int clientID){
        SocketClientHandler socketClientHandler = getSocketClientFromID(clientID);
        if(socketClientHandler == null)
            sendToSpecificRMI(serverAnswer, clientID);
        else
            socketClientHandler.publishSocketMessage(serverAnswer);
    }

    public void sendToEverybody(ServerAnswer serverAnswer){
        sendToEverybodySocket(serverAnswer);
        sendToEverybodyRMI(serverAnswer);
    }

    private void sendToEverybodySocket(ServerAnswer serverAnswer){
        for(SocketClientHandler socketClientHandler : socketServer.getClientsAdded()){
            socketClientHandler.publishSocketMessage(serverAnswer);
        }
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

    public int getLastClientIdAdded(){
        return this.lastClientIdAdded;
    }

    public Controller getController(){
        return this.controller;
    }

    public void setGameProxy(GameProxyInterface gameProxy){
        this.gameProxy = gameProxy;
    }

    public void setInitialSkulls(int initialSkulls){
        if(initialSkulls >= Constants.MIN_SKULLS && initialSkulls <= Constants.MAX_SKULLS)
            this.initialSkulls = initialSkulls;
        else
            this.initialSkulls = Constants.MIN_SKULLS;
    }

    public int getMapChoice() {
        return mapChoice;
    }

    public void setMapChoice(int mapChoice) {
        this.mapChoice = mapChoice;
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

    public String getRemoteObjectName(){  //RETURNS the name of the remote object
        return REGISTRATION_ROOM_NAME;
    }

    public static void main(String[] args) throws RemoteException{
        Server server = new Server();
        ExecutorService executor = Executors.newCachedThreadPool();

        server.socketServer = new SocketServer(1337, server);
        server.serverRMI = new ServerRMI(server);

        executor.submit(server.serverRMI);
        executor.submit(server.socketServer);
    }


}
