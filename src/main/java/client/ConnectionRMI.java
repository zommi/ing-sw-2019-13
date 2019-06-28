package client;

import constants.Constants;
import exceptions.GameAlreadyStartedException;
import server.GameProxyInterface;
import server.model.map.GameMap;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;
import view.ServerAnswer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ConnectionRMI extends UnicastRemoteObject implements Serializable, Connection, ReceiverInterface {

    private GameProxyInterface gameProxy;

    private boolean error = false;
    private GameModel gameModel;
    private int clientID;
    //private static final String SERVER_ADDRESS  = "192.168.43.66";
    private static final String SERVER_ADDRESS  = "localhost";
    private String serverAddress;
    private static final String REGISTRATION_ROOM_NAME = "gameproxy";
    private String registrationRoom;
    private static final int REGISTRATION_PORT = 1099;
    private int registrationPort;

    public ConnectionRMI() throws RemoteException{
        this.clientID = -1;
        this.gameModel = new GameModel();
        getProperties();
    }

    private void getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(Constants.PATH_TO_CONFIG));
            serverAddress = properties.getProperty("app.serverIp");
            registrationPort = Integer.valueOf(properties.getProperty("app.registryPort"));
            registrationRoom = properties.getProperty("app.registrationRoomName");
        } catch (IOException e) {
            System.out.println("File not found, given default values");
            serverAddress = SERVER_ADDRESS;
            registrationPort = REGISTRATION_PORT;
            registrationRoom = REGISTRATION_ROOM_NAME;
        }
    }

    public String getCurrentCharacterName(){
        try{
            return gameProxy.getCurrentCharacter(clientID);
        }
        catch(RemoteException e){
            System.out.println("Exception caught");
        }
        return null;
    }

    @Override
    public int getStartGame(){
        try{
            return gameProxy.getStartGame(clientID);
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
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
        try{
            return gameProxy.getGrenadeID(clientID);
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean askClient() throws RemoteException{
        return gameModel.getClientChoice();
    }

    @Override
    public int getNumberOfGrenades(){
        return gameModel.getNumberOfGrenades();
    }

    @Override
    public List<Info> getGrenadeAction(){
        return gameModel.getGrenadeAction();
    }

    @Override
    public int getCurrentID(){
        try{
            return gameProxy.getCurrentID(clientID);
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getClientID() {
        return clientID;
    }

    public void send(Info action){
        gameModel.setJustDidMyTurn(true);
        try {
            if (action instanceof NameInfo) {
                gameProxy.saveName(this, action);
            }
            else if(action instanceof SetupInfo){
                gameProxy.saveSetup(this, action);
            }
            else if(action instanceof ReconnectInfo){
                gameProxy.reconnect(((ReconnectInfo) action).getClientId());
            }
            else{
                gameProxy.makeAction(clientID, action);

            }
        }catch(RemoteException e){
            System.out.println("Remote exception caught");
            e.printStackTrace();
            gameModel.setJustDidMyTurn(false);
        }

    }

    public void sendAsynchronous(Info action){
        try{
            gameProxy.makeAsynchronousAction(this.clientID, action);
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

    public boolean getError(){
        return this.error;
    }

    @Override
    public void configure(String name) {
        try{
            System.out.println("Connecting to the Remote Object... ");

            System.out.println("Connecting to the registry... ");
            Registry registry = LocateRegistry.getRegistry(serverAddress,registrationPort);
            gameProxy = (GameProxyInterface) registry.lookup(registrationRoom);

            System.out.println("Registering... ");

            if(gameProxy == null)
                this.error = true;

            gameProxy.saveName(this, new NameInfo(name));
        }
        catch(RemoteException|NotBoundException re){
            re.printStackTrace();
            System.out.println("Exception while initializing");
        }
    }

    @Override
    public void setClientIDExisting(int idAlreadyExisting) throws RemoteException{
        this.clientID = idAlreadyExisting;
    }

    @Override
    public void setClientId(int clientId) {
        this.clientID = clientId;
        startPing();
    }

    @Override
    public void ping() {
        //
    }

    private void startPing() {
        new Thread(){
            private boolean keepThreadAlive = true;

            @Override
            public void run(){
                while(keepThreadAlive) {
                    try {
                        gameProxy.ping();
                        TimeUnit.SECONDS.sleep(Constants.PING_DELAY_SEC);
                    } catch (RemoteException e) {
                        System.out.println("Remote exception after ping");
                        keepThreadAlive = false;
                        gameModel.setServerOffline(true);
                    }catch(InterruptedException e1){
                        //
                    }
                }
            }
        }.start();
    }
}

