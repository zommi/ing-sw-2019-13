package server;

import client.Connection;
import client.Info;
import client.ReceiverInterface;
import exceptions.GameAlreadyStartedException;
import server.model.map.GameMap;
import server.model.player.*;
import view.ServerAnswer;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GameProxy extends Publisher implements GameProxyInterface, Serializable {

    private ReceiverInterface clientRMI;
    private int startGame;
    private GameMap map;
    private int numMap;
    private String playerName;
    private ServerRMI serverRMI;
    private List<PlayerAbstract> player = new ArrayList();
    private int clientIDadded;
    private int initialSkulls;
    private List<ReceiverInterface> clientRMIadded = new ArrayList<>();


    protected GameProxy(ServerRMI serverRMI) throws RemoteException {
        this.serverRMI = serverRMI;
        this.serverRMI.getServer().setGameProxy(this);
        UnicastRemoteObject.exportObject(this, 1099);
    }

    public String getCurrentCharacter() throws RemoteException{
        return this.serverRMI.getServer().getController().getCurrentCharacter();
    }

    public List<ReceiverInterface> getClientRMIadded() throws RemoteException{
        return this.clientRMIadded;
    }

    @Override
    public boolean isCharacterTaken(String nameChar) throws RemoteException{
        System.out.println("Checking if the character is already taken by someone else");
        System.out.println("In my list I have " +player.size() +"players, i will check if they already have chosen their characters");
        for(int i = 0; i < player.size(); i++){
            if((player.get(i).getIfCharacter() == true)&&(player.get(i).getCharacterName().equalsIgnoreCase(nameChar))){
                System.out.println("Found " +player.get(i).getCharacterName());
                System.out.println("The character is already taken by someone else");
                return true;
            }
        }
        System.out.println("The character name you chose is ok");
        return false;
    }

    @Override
    public boolean makeAction(int clientID, Info action)  throws RemoteException{
        this.serverRMI.getServer().getController().makeAction(clientID, action);
        return true;
    }

    /*
    @Override
    public void setClientHasChosenPowerup() {
        this.serverRMI.getServer().getController().setClientHasChosen();
    }*/

    @Override
    public Info getGrenadeAction(int ID) throws RemoteException{
        for (int k = 0; k < clientRMIadded.size(); k++) {
            try {
                int clientID = clientRMIadded.get(k).getClientID();
                if (clientID == ID) {
                    return clientRMIadded.get(k).getGrenadeAction();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean askClient(int ID) throws RemoteException{
        for(int k = 0; k < clientRMIadded.size(); k++){
            try{
                int clientID = clientRMIadded.get(k).getClientID();
                if(clientID == ID){
                    return clientRMIadded.get(k).askClient();
                }
            }
            catch(RemoteException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean makeAsynchronousAction(int clientID, Info action)  throws RemoteException{
        this.serverRMI.getServer().getController().makeAsynchronousAction(clientID, action);
        return true;
    }

    public void addClientRMI(ReceiverInterface receiver){
        this.clientRMIadded.add(receiver);
    }

    @Override
    public void sendMessage(ServerAnswer message){
    }

    @Override
    public int getCurrentID() throws RemoteException{
        return this.serverRMI.getServer().getController().getCurrentID();
    }

    @Override
    public int getGrenadeID() throws RemoteException{
        return this.serverRMI.getServer().getController().getGrenadeID();
    }

    @Override
    public void register(ReceiverInterface client) throws RemoteException, NotBoundException, GameAlreadyStartedException{
        System.out.println("Adding the client to the server...");
        if(serverRMI.getServer().getStartGame() == 1){
            throw new GameAlreadyStartedException();
        };
        this.clientIDadded = serverRMI.addClient(client);
        System.out.println("Added client number: " +clientIDadded);


        /*System.out.println("I am trying to connect to the client");
        Registry registryClient = LocateRegistry.getRegistry("localhost",1000);

        client = (ReceiverInterface) registryClient.lookup("rmiconnection");
        System.out.println("I just connected to the client");*/
    }

    @Override
    public int getClientID() throws RemoteException{
        return this.clientIDadded;
    }

    @Override
    public void setClientRMI(ReceiverInterface clientRMI) throws RemoteException{
        System.out.println("Trying to connect the server to the client");
        this.clientRMI = clientRMI;
        //clientRMI.print();
        this.addClientRMI(clientRMI);
        System.out.println("I just connected to the client");
    }

    @Override
    public boolean sendPlayer(String name, int clientID)  throws RemoteException{
        System.out.println("Name received");
        if(player.size() == 5)
            return false;
        PlayerAbstract playerToAdd = new ConcretePlayer(name);
        playerToAdd.setState(PlayerState.TOBESPAWNED);
        playerToAdd.setIfCharacter(false);
        ((ConcretePlayer) playerToAdd).setClientID(clientID);
        player.add(playerToAdd);
        return true;
    }

    @Override
    public boolean addPlayerCharacter(String name, int ID) throws RemoteException{
        for(int i = 0; i < player.size(); i++){
            if(player.get(i).getClientID() == ID){
                serverRMI.getServer().addPlayer(player.get(i));
                this.player.get(i).setPlayerCharacter(Figure.fromString(name));
                this.player.get(i).setIfCharacter(true);
                this.serverRMI.getServer().getController().getCurrentGame().getCurrentGameBoard().addGameCharacter(new GameCharacter(Figure.fromString(name)));
            }
            System.out.println("For now I have received client number " +player.get(i).getClientID());
        }
        return true;
    }

    @Override
    public GameMap getMap() throws RemoteException{
        return this.map;
    }

    @Override
    public String getMapName() throws RemoteException{
        if(this.numMap == 0)
            return "Map 1";
        else if(this.numMap == 1)
            return "Map 2";
        else if(this.numMap == 2)
            return "Map 3";
        else if(this.numMap == 3)
            return "Map 4";
        else
            return "No one has chosen yet";
    }

    @Override
    public boolean sendInitialSkulls(int initialSkulls) throws RemoteException{
        this.initialSkulls = initialSkulls;
        serverRMI.getServer().setInitialSkulls(initialSkulls);
        System.out.println("Initial skulls choice received");
        return true;
    }

    @Override
    public int getInitialSkulls() throws RemoteException{
        return this.initialSkulls;
    }

    /*
    @Override
    public void startMatch() throws RemoteException{
        System.out.println("Starting the match");
        startGame = this.serverRMI.getServer().startMatch();
    }*/


    @Override
    public boolean sendMap(int numMap)  throws RemoteException{
        System.out.println("Map choice received");
        this.numMap = numMap;
        //System.out.println("Test 0"); //this works
        serverRMI.getServer().setMap(numMap); //then the problem is here
        //System.out.println("Test 1"); //this doesn't work
        serverRMI.setController(serverRMI.getServer().getController());
        System.out.println("Controller set for serverRMI");
        return true;
    }

    @Override
    public boolean addMapPlayer(int clientID) throws RemoteException{
        serverRMI.addMapClient(clientID);
        System.out.println("Added the map of the client and of the player");
        return true;
    }

    @Override
    public PlayerAbstract getPlayer(int clientID) throws RemoteException{
        for(int i = 0; i < player.size(); i++){
            if(player.get(i).getClientID() == clientID)
                return player.get(i);
        }
        return null;
    }

    @Override
    public int getStartGame() throws RemoteException{
        return this.serverRMI.getServer().getStartGame();
    }
}

