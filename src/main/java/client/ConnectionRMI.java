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
        System.setProperty("rmi.server.hostname","192.168.1.5");
    }

    private void getProperties() {
        Properties properties = new Properties();
        try {

            FileInputStream configFile = new FileInputStream(Constants.PATH_TO_CONFIG);
            properties.load(configFile);
            configFile.close();

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

    @Override
    public void setStartGame(int startGame) {

    }

    @Override
    public void setCurrentID(int currentID) {

    }

    @Override
    public void setCurrentCharacter(String currentCharacter) {

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
        if(gameModel.isGameOver())
            return;

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
        if(gameModel.isGameOver())
            return;

        gameModel.setJustDidMyTurn(true);

        try{
            gameProxy.makeAsynchronousAction(this.clientID, action);
        }
        catch(Exception re){
            System.out.println("Could not make the action");
            re.printStackTrace();
            gameModel.setJustDidMyTurn(false);
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

