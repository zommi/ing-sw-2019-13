package client;

import exceptions.GameAlreadyStartedException;
import server.GameProxyInterface;
import server.model.map.GameMap;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;
import view.ServerAnswer;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ConnectionRMI extends UnicastRemoteObject implements Serializable, Connection, ReceiverInterface {

    private GameProxyInterface gameProxy;
    private String mapChoice;
    private int initialSkulls;

    private boolean error = false;
    private boolean playerNameSet = false;
    private boolean initialSkullsSet = false;
    private boolean characterNameSet = false;
    private boolean mapSet = false;
    private GameModel gameModel;
    private GameProxyInterface game;
    private int clientID;
    //private static final String SERVER_ADDRESS  = "192.168.43.66";
    private static final String SERVER_ADDRESS  = "localhost";
    private static final String REGISTRATION_ROOM_NAME = "gameproxy";
    private static final int REGISTRATION_PORT = 1099;

    public ConnectionRMI(int clientID) throws RemoteException{
        this.clientID = clientID;
        this.gameModel = new GameModel();
    }

    public String getCurrentCharacterName(){
        try{
            return gameProxy.getCurrentCharacter();
        }
        catch(RemoteException e){
            System.out.println("Exception caught");
        }
        return null;
    }

    /*@Override
    public void setClientHasChosen(){
        try{
            gameProxy.setClientHasChosenPowerup();
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
    }*/

    @Override
    public int getStartGame(){
        try{
            return gameProxy.getStartGame();
        }
        catch(RemoteException e)
        {
            System.out.println("Exception while starting the game");
        }
        return 0;
    }

    public void sendConnection() {

    }


    public void setClientID(int clientID){
        this.clientID = clientID;
    }

    @Override
    public void setMapNum(int mapNum) {

    }

    @Override
    public void setInitialSkulls(int initialSkulls) {

    }

    @Override
    public void setStartGame(int startGame) {

    }

    @Override
    public void setGrenadeID(int grenadeID) {

    }

    @Override
    public void setCurrentID(int currentID) {

    }

    @Override
    public void setCurrentCharacter(String currentCharacter) {

    }

    public GameProxyInterface getGameProxy(){
        return this.gameProxy;
    }

    @Override
    public int getGrenadeID(){
            return gameModel.getGrenade();
    }

    @Override
    public boolean askClient() throws RemoteException{
        return gameModel.getClientChoice();
    }


    @Override
    public List<Info> getGrenadeAction(){
        return gameModel.getGrenadeAction();
    }

    @Override
    public int getCurrentID(){
        try{
            return gameProxy.getCurrentID();
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public boolean isCharacterChosen(String name) {
        boolean found = false;
        for(Figure figure : Figure.values()){
            if(figure.name().equalsIgnoreCase(name))
                found = true;
        }
        if (found) {
            try{
                if(gameProxy.isCharacterTaken(name)){
                    return false;
                }
                else {
                    return true;
                }
            }
            catch(RemoteException re){
                System.out.println("Exception caught");
            }
        }
        return false;
    }


    @Override
    public int getClientID() {
        return clientID;
    }

    public void send(Info action){
        try{
            game.makeAction(this.clientID, action);
        }
        catch(Exception re){
            System.out.println("Could not make the action");
            re.printStackTrace();
        }
    }

    public void sendAsynchronous(Info action){
        try{
            game.makeAsynchronousAction(this.clientID, action);
        }
        catch(Exception re){
            System.out.println("Could not make the action");
            re.printStackTrace();
        }
    }

    @Override
    public void publishMessage(ServerAnswer answer){
        gameModel.saveAnswer(answer);
    }

    @Override
    public GameModel getGameModel(){
        return this.gameModel;
    }


    @Override
    public int getInitialSkulls(){
        try {
            return gameProxy.getInitialSkulls();
        }
        catch (RemoteException re){
            System.out.println("Could not get the initial skulls from the server");
            re.printStackTrace();
        }
        return 0;
    }

    /*@Override
    public void startMatch(){
        try{
            gameProxy.startMatch();
        }
        catch (RemoteException e){
            System.out.println("Exception while starting the timer");
        }
    }*/

    public boolean getError(){
        return this.error;
    }

    @Override
    public void configure() {
        try{
            this.game = initializeRMI();
            if(game == null)
                this.error = true;
        }
        catch(RemoteException|NotBoundException re){
            System.out.println("Exception while initializing");
        }
    }

    public void print() throws RemoteException{
        System.out.println("Test Connection");
    }


    public GameProxyInterface initializeRMI() throws RemoteException, NotBoundException {
        System.out.println("Connecting to the Remote Object... ");

        System.out.println("Connecting to the registry... ");
        Registry registry = LocateRegistry.getRegistry(SERVER_ADDRESS,REGISTRATION_PORT);
        gameProxy = (GameProxyInterface) registry.lookup(REGISTRATION_ROOM_NAME);

        System.out.println("Registering... ");

        /*Registry registryClient = LocateRegistry.createRegistry(1000);
        registryClient.bind(this.name, this);
        System.out.println("I am exporting the remote object...");
        (ReceiverInterface) UnicastRemoteObject.exportObject(this, 1000)*/

        try{
            gameProxy.register(this);
        }
        catch(GameAlreadyStartedException e){
            System.out.println("The Game already started!");
            return null;
        }
        setClientID(gameProxy.getClientID());
        gameProxy.setClientRMI(clientID, this);
        this.gameModel.setClientID(clientID);

        System.out.println("Your ClientID is " + this.clientID);

        return gameProxy;
    }

    public void addPlayerCharacter(String name){
        System.out.println("Trying to send the name of your character to the server...");
        while (!characterNameSet) {
            try{
                characterNameSet = gameProxy.addPlayerCharacter(name.toUpperCase(), clientID);
                gameProxy.addMapPlayer(clientID);
                characterNameSet = true;
                System.out.println("Name sent to to the server!");
            }
            catch(RemoteException re){
                System.out.println("Could not send the character");
                re.printStackTrace();
            }
        }
    }

    @Override
    public String getCharacterName(){
        try{
            return gameProxy.getCharacterName(clientID);
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
        return "No name yet";
    }

    public void startTimer() {

    }

    public void saveAnswer(ServerAnswer answer) {

    }


    @Override
    public void add(String playerName, int mapClient, int initialSkulls){
        //gameProxy.addClient(player, this.clientID); //i add a line in the hashmap of the gameModel

        if(clientID == 0) {
            System.out.println("Trying to send your choice of initial skulls to the server...");
            while (!initialSkullsSet) {
                try {
                    initialSkullsSet = gameProxy.sendInitialSkulls(initialSkulls);
                    initialSkullsSet = true;
                } catch (RemoteException re) {
                    System.out.println("Could not send the initial skulls");
                    re.printStackTrace();
                }
            }
            try{
                this.initialSkulls = gameProxy.getInitialSkulls();
            }
            catch(RemoteException e){
                System.out.println("Remote Exception");
            }
        }

        //devo guardare che non ci sia già.

        System.out.println("Trying to send your name to the server...");
        while (!playerNameSet) {
            try{
                playerNameSet = gameProxy.sendPlayer(playerName, clientID);
            }
            catch(RemoteException re){
                System.out.println("Could not send the player");
                re.printStackTrace();
            }
        }

        System.out.println("The server received your name...");

        if(clientID == 0){
            System.out.println("Sending your chosen map to the server...");
            while(!mapSet){
                try{
                    mapSet = gameProxy.sendMap(mapClient);
                    mapSet = true;
                }
                catch(RemoteException re){
                    System.out.println("Could not send the map");
                }
            }
            System.out.println("The server received your choice of the map...");
            try{
                this.mapChoice = gameProxy.getMapName();
            }
            catch(RemoteException e){
                System.out.println("Remote Exception caught");
            }

        }

        //TODO manca la parte in cui salvo le scelte del client!
    }


    @Override
    public String getMapName() {
        try{
            return gameProxy.getMapName();
        }
        catch (RemoteException re){
            System.out.println("Could not take the map name from the server");
            re.printStackTrace();
        }
        return "No one has chosen yet";
    }
}

