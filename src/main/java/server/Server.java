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

    public Client getClientFromId(int clientID){
        return idToClient.get(clientID);
    }

    public Integer getIdFromName(String name){
        return nameToId.get(name);
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

    public void disconnect(int clientID){
        getClientFromId(clientID).disconnect();
    }

    public static void main(String[] args) throws RemoteException {

        Server server = new Server();

        ExecutorService executor = Executors.newCachedThreadPool();

        executor.submit(server.serverRMI);
        executor.submit(server.socketServer);
    }

}
