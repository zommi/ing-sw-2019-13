package client;

import server.GameProxy;
import server.GameProxyInterface;
import server.ServerAnswer.ServerAnswer;
import server.controller.playeraction.Action;
import server.model.gameboard.GameBoard;
import server.model.map.GameMap;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class ConnectionRMI extends Connection implements Serializable, ReceiverInterface, Remote {

    private GameProxyInterface gameProxy;
    private int mapChoice;
    private boolean playerNameSet = false;
    private boolean mapSet = false;
    private GameModel gameModel;
    private GameProxyInterface game;
    private int clientID;
    private static final String SERVER_ADDRESS  = "localhost";
    private static final String REGISTRATION_ROOM_NAME = "gameproxy";
    private static final int REGISTRATION_PORT = 1099;
    public ConnectionRMI(int clientID){
        this.clientID = clientID;
        this.gameModel = new GameModel(clientID);
    }

    @Override
    public void send(Action action){
        boolean serverAnswer = game.makeAction(this.clientID, action);
    }

    @Override
    public void publishMessage(ServerAnswer answer){
    //TODO has to store the answer in the ClientModel
        //this.ClientModel.storeResponse(message);
        return;
    }

    @Override
    public GameModel getGameModel(){
        return this.gameModel;
    }


    @Override
    public void configure() throws RemoteException, NotBoundException {
        this.game = initializeRMI();
    }

    @Override
    public GameProxyInterface initializeRMI() throws RemoteException, NotBoundException {
        System.out.println("Connecting to the Remote Object... ");

        System.out.println("Connecting to the registry... ");
        Registry registry = LocateRegistry.getRegistry(SERVER_ADDRESS,REGISTRATION_PORT);

        gameProxy = (GameProxyInterface) registry.lookup(REGISTRATION_ROOM_NAME);

        System.out.println("Registering... ");
        gameProxy.register((ReceiverInterface) UnicastRemoteObject.exportObject(this, 0));

        System.out.println("Your ClientID is " + this.clientID);

        return gameProxy;
    }


    @Override
    public void add(PlayerAbstract player, int map){
        gameModel.addClient(player, this.clientID); //i add a line in the hashmap of the gameModel

        System.out.println("Trying to send your name to the server...");

        while (playerNameSet == false) {
            playerNameSet = gameProxy.sendPlayerName(player.getName());
        }

        System.out.println("The server received your name...");

        if(clientID == 0){
            System.out.println("Sending your chosen map to the server...");
            while(mapSet == false){
                mapSet = gameProxy.sendMap(map);
            }
            System.out.println("The server received your choice of the map...");
        }

        //TODO manca la parte in cui salvo le scelte del client!

    }
}
