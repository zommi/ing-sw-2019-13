package server;

import client.*;
import constants.Constants;
import server.model.player.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameProxy implements GameProxyInterface, Serializable {

    private ServerRMI serverRMI;
    private List<PlayerAbstract> player = new ArrayList<>();
    private Map<Integer, ReceiverInterface> clientRMIadded = new HashMap<>();


    protected GameProxy(ServerRMI serverRMI) throws RemoteException {
        this.serverRMI = serverRMI;
        UnicastRemoteObject.exportObject(this, 1099);
    }

    public String getCurrentCharacter(int clientID) throws RemoteException{
        return serverRMI.getGameManagerFromId(clientID).getController().getCurrentCharacterName();
    }


    public int getClientID(ReceiverInterface receiverInterface){
        for(Map.Entry<Integer, ReceiverInterface> entry : clientRMIadded.entrySet()){
            if(entry.getValue() == receiverInterface)
                return entry.getKey();
        }
        return -2; //this should never happen
    }

    @Override
    public void makeAction(int clientID, Info action)  throws RemoteException{
        serverRMI.getGameManagerFromId(clientID).getController().makeAction(clientID, action);
    }

    @Override
    public void saveName(ReceiverInterface receiverInterface, Info info) throws RemoteException {
        NameInfo nameInfo = (NameInfo) info;

        Integer clientID = serverRMI.getServer().addConnection(nameInfo.getName(), "rmi",
                null, receiverInterface);        //automatically sends setupAnswer
        if(clientID != null){
            receiverInterface.setClientId(clientID);
                new Thread(){
                    private boolean keepThreadAlive = true;

                    @Override
                    public void run(){
                        while(keepThreadAlive) {
                            try {
                                receiverInterface.ping();
                                TimeUnit.SECONDS.sleep(Constants.PING_DELAY_SEC);
                            } catch (RemoteException e) {
                                System.out.println("Remote exception after ping");
                                keepThreadAlive = false;
                                Client client = serverRMI.getServer().getClientFromId(clientID);
                                if(client.getGameManager().isGameOver())
                                    serverRMI.getServer().removeClient(clientID);
                                else
                                    client.disconnect();

                            }catch(InterruptedException e1){
                                //
                            }
                        }
                    }
                }.start();
        }
    }

    @Override
    public void saveSetup(ReceiverInterface receiverInterface, Info info) throws RemoteException {
        SetupInfo setupInfo = (SetupInfo) info;
        serverRMI.getServer().setupPlayer(receiverInterface.getClientID(), setupInfo);
    }

    public void makeAsynchronousAction(int clientID, Info action)  throws RemoteException{
        this.serverRMI.getGameManagerFromId(clientID).getController().makeAsynchronousAction(clientID, action);
    }

    @Override
    public int getCurrentID(int clientID) throws RemoteException{
        return this.serverRMI.getGameManagerFromId(clientID).getController().getCurrentID();
    }

    @Override
    public void reconnect(int clientId) {
        Client client = serverRMI.getServer().getClientFromId(clientId);
        client.getGameManager().reconnect(client.getPlayer());
    }

    @Override
    public void ping() throws RemoteException {
        //
    }

    @Override
    public int getStartGame(int clientID) throws RemoteException{
        return this.serverRMI.getGameManagerFromId(clientID).getGameStarted();
    }
}

