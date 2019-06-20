package server;

import client.ReceiverInterface;
import client.SetupInfo;
import server.model.game.GameState;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerState;
import view.SetupRequestAnswer;
import view.SetupConfirmAnswer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int lastClientIdAdded;
    private List<Integer> listOfClients = new ArrayList<>();

    private ServerRMI serverRMI;
    private SocketServer socketServer;

    private List<GameManager> gameManagerList;
    private GameManager currentGameManager;

    private Map<Integer, Client> idToClient;

    private Map<String, Integer> nameToId;


    public Server() throws RemoteException{

        idToClient = new HashMap<>();
        nameToId = new HashMap<>();

        gameManagerList = new ArrayList<>();
        currentGameManager = new GameManager(this);
        gameManagerList.add(currentGameManager);

        socketServer = new SocketServer(1337, this);
        serverRMI = new ServerRMI(this);
    }

    public Map<String, Integer> getNameToId() {
        return nameToId;
    }

    public Client getClientFromId(int clientID){
        return idToClient.get(clientID);
    }

    public Integer getIdFromName(String name){
        return nameToId.get(name);
    }

    public String getNameFromId(int clientId){
        return idToClient.get(clientId).getName();
    }

    public GameProxyInterface getGameProxy(){
        return serverRMI.getGameProxy();
    }

    public synchronized int addClient(){
        if(listOfClients.isEmpty()){
            listOfClients.add(0);
            lastClientIdAdded = 0;
        }
        else{ //it is not the first element added in the list so it is not the first client.
            listOfClients.add(lastClientIdAdded +1);
            lastClientIdAdded = lastClientIdAdded + 1;
        }
        System.out.println("Added the clientID " + lastClientIdAdded);

        return lastClientIdAdded;
    }

    public synchronized void addClient(Client client){
        idToClient.put(client.getClientID(), client);
    }

    public synchronized Integer addConnection(String name, String connectionType, SocketClientHandler socketClientHandler, ReceiverInterface receiverInterface){
        SetupRequestAnswer setupRequestAnswer = new SetupRequestAnswer();

        Integer clientID = getIdFromName(name);
        if(clientID == null){
            //player has never connected before
            clientID = addClient();     //just gets the id
            Client client = new Client(clientID, getCurrentGameManager(), name);

            if(connectionType.equalsIgnoreCase("socket"))
                client.setSocketClientHandler(socketClientHandler);
            else if(connectionType.equalsIgnoreCase("rmi"))
                client.setReceiverInterface(receiverInterface);

            addClient(client);          //adds client to the hashmap

            nameToId.put(name, clientID);    //adds name to the hashmap

            if(client.getGameManager().isNoPlayer()){                       //decides skulls and map
                client.getGameManager().setNoPlayer(false);
                setupRequestAnswer.setFirstPlayer(true);
                client.setFirstPlayer(true);
            }

            setupRequestAnswer.setGameCharacter(true);
            setupRequestAnswer.setClientID(clientID);

            client.send(setupRequestAnswer);

        }else{
            //player is connecting, not for the first time
            //it may be already disconnected or still active

            Client client = getClientFromId(clientID);
            if(!client.isConnected()){
                if(connectionType.equalsIgnoreCase("socket"))
                    client.setSocketClientHandler(socketClientHandler);
                else if(connectionType.equalsIgnoreCase("rmi"))
                    client.setReceiverInterface(receiverInterface);
            }

            //setting player connected so that his turn will be played
            client.getGameManager().getController().getCurrentGame().getPlayerFromId(clientID).setConnected(true);

            //empty setupAnswer
            setupRequestAnswer.setClientID(clientID);
            client.send(setupRequestAnswer);
        }

        return clientID;
    }

    public synchronized void setupPlayer(int clientId, SetupInfo setupInfo){

        Client client = getClientFromId(clientId);
        GameManager gameManager = client.getGameManager();

        if(client.isPlayerSetupComplete() || (gameManager.isSetupComplete() &&
                gameManager.getController().getCurrentGame().getCurrentState() != GameState.SETUP))
            return;

        if(client.isFirstPlayer() && !gameManager.isSetupComplete()) {
            //the gamemanager will manage illegal values
            gameManager.setInitialSkulls(setupInfo.getInitialSkulls());
            gameManager.setMapChoice(setupInfo.getMapChoice());

            //now we can call createController because map and skulls have been set
            gameManager.createController();

            gameManager.setSetupComplete(true);
        }

        ConcretePlayer concretePlayer = new ConcretePlayer(client.getName());
        concretePlayer.setClientID(clientId);
        concretePlayer.setState(PlayerState.TOBESPAWNED);

        if(Figure.fromString(setupInfo.getCharacterName()) != null && !gameManager.isCharacterTaken(setupInfo.getCharacterName())){
            //player in Game has already been created
            concretePlayer.setPlayerCharacter(Figure.fromString(setupInfo.getCharacterName()));
        }
        else{
            concretePlayer.setPlayerCharacter(gameManager.getFreeFigure());
        }

        //adding the player to the game
        gameManager.addPlayer(concretePlayer);

        //player setup is ok
        client.setPlayerSetupComplete(true);

        //sends an answer with updated data
        SetupConfirmAnswer setupConfirmAnswer = new SetupConfirmAnswer();
        setupConfirmAnswer.setCharacterName(concretePlayer.getCharacterName());

        if(gameManager.isSetupComplete()){
            //sends game related info
            setupConfirmAnswer.setMapNum(gameManager.getMapChoice());
            setupConfirmAnswer.setSkullNum(gameManager.getInitialSkulls());
        }

        client.send(setupConfirmAnswer);
    }

    public GameManager getGameManagerFromId(int clientID){
        return idToClient.get(clientID).getGameManager();
    }

    public GameManager getCurrentGameManager() {
        return currentGameManager;
    }

    public void setCurrentGameManager(GameManager currentGameManager) {
        this.currentGameManager = currentGameManager;
        gameManagerList.add(currentGameManager);
    }

    public static void main(String[] args) throws RemoteException {

        Server server = new Server();

        ExecutorService executor = Executors.newCachedThreadPool();

        executor.submit(server.serverRMI);
        executor.submit(server.socketServer);
    }

}
