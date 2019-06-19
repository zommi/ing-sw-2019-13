package server;

import client.ReceiverInterface;

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

    private Map<Integer, GameManager> idToGameManager;

    private Map<Integer, Client> idToClient;


    public Server() throws RemoteException{

        idToGameManager = new HashMap<>();
        idToClient = new HashMap<>();

        gameManagerList = new ArrayList<>();
        currentGameManager = new GameManager(this);
        gameManagerList.add(currentGameManager);

        socketServer = new SocketServer(1337, this);
        serverRMI = new ServerRMI(this);
    }

    public Client getClientFromId(int clientID){
        return idToClient.get(clientID);
    }


    public GameProxyInterface getGameProxy(){
        return serverRMI.getGameProxy();
    }

    public synchronized int addClient(GameManager gameManager){
        if(listOfClients.isEmpty()){
            listOfClients.add(0);
            lastClientIdAdded = 0;
        }
        else{ //it is not the first element added in the list so it is not the first client.
            listOfClients.add(lastClientIdAdded +1);
            lastClientIdAdded = lastClientIdAdded + 1;
        }
        System.out.println("Added the clientID " + lastClientIdAdded);

        //add entry to hashmap
        idToGameManager.put(lastClientIdAdded, gameManager);

        return lastClientIdAdded;
    }

    public GameManager getGameManagerFromId(int clientID){
        return idToGameManager.get(clientID);
    }

    public GameManager getCurrentGameManager() {
        return currentGameManager;
    }

    public void setCurrentGameManager(GameManager currentGameManager) {
        this.currentGameManager = currentGameManager;
        gameManagerList.add(currentGameManager);
    }

    public void disconnect(int clientID){

    }

    public static void main(String[] args) throws RemoteException {

        Server server = new Server();

        ExecutorService executor = Executors.newCachedThreadPool();

        executor.submit(server.serverRMI);
        executor.submit(server.socketServer);
    }

}
