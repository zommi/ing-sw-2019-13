package server;

import client.*;
import constants.Constants;
import exceptions.GameAlreadyStartedException;
import server.model.gameboard.GameBoard;
import server.model.player.*;
import view.*;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameProxy extends Publisher implements GameProxyInterface, Serializable {

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
    public boolean makeAction(int clientID, Info action)  throws RemoteException{
        serverRMI.getGameManagerFromId(clientID).getController().makeAction(clientID, action);
        return true;
    }

    @Override
    public void saveName(ReceiverInterface receiverInterface, Info info) throws RemoteException {
        NameInfo nameInfo = (NameInfo) info;

        Integer clientID = serverRMI.getServer().addConnection(nameInfo.getName(), "rmi",
                null, receiverInterface);        //automatically sends setupAnswer
        if(clientID != null){
            receiverInterface.setClientId(clientID);
        }
    }

    @Override
    public void saveSetup(ReceiverInterface receiverInterface, Info info) throws RemoteException {
        SetupInfo setupInfo = (SetupInfo) info;
        serverRMI.getServer().setupPlayer(receiverInterface.getClientID(), setupInfo);
    }

    @Override
    public List<Info> getGrenadeAction(int ID) throws RemoteException{
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
    public String getCharacterName(int clientID) throws RemoteException{
        for(PlayerAbstract p:player){
            if(p.getGameCharacter() != null){ //if the character has already been assigned it means that the player already exists in the list and it's completed, otherwise the player still does not have a character
                if(p.getClientID() == clientID)
                    return p.getCharacterName();
            }
        }
        return "No name yet";
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
        this.serverRMI.getGameManagerFromId(clientID).getController().makeAsynchronousAction(clientID, action);
        return true;
    }

    @Override
    public void sendMessage(ServerAnswer message){
    }

    @Override
    public int getCurrentID(int clientID) throws RemoteException{
        return this.serverRMI.getGameManagerFromId(clientID).getController().getCurrentID();
    }

    @Override
    public int getGrenadeID(int clientID) throws RemoteException{
        return this.serverRMI.getGameManagerFromId(clientID).getController().getGrenadeID();
    }

    @Override
    public void reconnect(int clientId) {
        Client client = serverRMI.getServer().getClientFromId(clientId);
        client.getGameManager().reconnect(client.getPlayer());
    }

    @Override
    public PlayerAbstract getPlayer(int clientID) throws RemoteException{
        for(PlayerAbstract p:player){
            if(p.getClientID() == clientID)
                return p;
        }
        return null;
    }

    @Override
    public int getStartGame(int clientID) throws RemoteException{
        return this.serverRMI.getGameManagerFromId(clientID).getStartGame();
    }
}

