package client;

import constants.Constants;
import server.GameProxyInterface;
import view.ServerAnswer;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;

public class ConnectionRMI extends UnicastRemoteObject implements Serializable, Connection, ReceiverInterface {

    private GameProxyInterface gameProxy;

    private boolean error = false;
    private GameModel gameModel;
    private int clientID;
    private String serverAddress;
    private String registrationRoom;
    private int registrationPort;

    public ConnectionRMI() throws RemoteException{
        this.clientID = -1;
        this.gameModel = new GameModel();
        serverAddress = Constants.SERVER_ADDRESS;
        registrationPort = Constants.RMI_PORT;
        registrationRoom = Constants.REGISTRATION_ROOM_NAME;
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

        gameModel.setGamemodelNotUpdated(true);
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
            gameModel.setGamemodelNotUpdated(false);
        }

    }

    public void sendAsynchronous(Info action){
        if(gameModel.isGameOver())
            return;

        gameModel.setGamemodelNotUpdated(true);

        try{
            gameProxy.makeAsynchronousAction(this.clientID, action);
        }
        catch(Exception re){
            System.out.println("Could not make the action");
            re.printStackTrace();
            gameModel.setGamemodelNotUpdated(false);
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

