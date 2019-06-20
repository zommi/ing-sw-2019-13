package server;

import client.Info;
import client.NameInfo;
import client.ReceiverInterface;
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

    private int numMap;
    private ServerRMI serverRMI;
    private List<PlayerAbstract> player = new ArrayList<>();
    private Integer clientIDadded;
    private boolean firstPlayer;
    private GameManager gameManager;
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

    /*public Map<Integer, ReceiverInterface> getClientRMIadded() throws RemoteException{
        return clientRMIadded;
    }*/

    @Override
    public List<ReceiverInterface> getClientsRMIadded() throws RemoteException {
        return new ArrayList<>(clientRMIadded.values());
    }

    @Override
    public boolean isCharacterTaken(String nameChar, int clientID) throws RemoteException{
        return serverRMI.getGameManagerFromId(clientID).isCharacterTaken(nameChar);
    }

    @Override
    public boolean makeAction(int clientID, Info action)  throws RemoteException{
        serverRMI.getGameManagerFromId(clientID).getController().makeAction(clientID, action);
        return true;
    }

    /*
    @Override
    public void setClientHasChosenPowerup() {
        this.serverRMI.getServer().getController().setClientHasChosen();
    }*/


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

    public void addClientRMI(int id, ReceiverInterface receiver){
        this.clientRMIadded.put(id, receiver);
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
    public void register(Info action, ReceiverInterface receiverInterface) throws RemoteException, NotBoundException, GameAlreadyStartedException{
        System.out.println("Adding the client to the server...");
        NameInfo nameInfo = (NameInfo) action;
        SetupAnswer setupAnswer = new SetupAnswer();

        clientIDadded = serverRMI.getServer().getIdFromName(nameInfo.getName());
        if(clientIDadded == null){
            clientIDadded = serverRMI.getServer().addClient();     //just gets the id
            gameManager = serverRMI.getServer().getCurrentGameManager();
            Client client = new Client(clientIDadded, gameManager);
            client.setReceiverInterface(receiverInterface);
            serverRMI.getServer().addClient(client);          //adds client to the hashmap

            if(gameManager.isNoPlayer()){                       //decides skulls and map
                gameManager.setNoPlayer(false);
                setupAnswer.setFirstPlayer(true);
                firstPlayer = true;
            }
            serverRMI.getServer().getNameToId().put(nameInfo.getName(), clientIDadded);
            setupAnswer.setGameCharacter(true);
        }
        else{
            gameManager = gameManager.getServer().getGameManagerFromId(clientIDadded);

            //reconnecting (with socket)
            gameManager.getServer().getClientFromId(clientIDadded).setReceiverInterface(receiverInterface);
            gameManager.getController().getCurrentGame().getPlayerFromId(clientIDadded).setConnected(true);
        }
        receiverInterface.publishMessage(setupAnswer);
        System.out.println("Added client number: " +clientIDadded);
    }

    @Override
    public int getClientID() throws RemoteException{
        return this.clientIDadded;
    }

    @Override
    public void setClientRMI(int id, ReceiverInterface clientRMI) throws RemoteException{
        System.out.println("Trying to connect the server to the client");
        addClientRMI(id, clientRMI);
        System.out.println("I just connected to the client");
    }

    @Override
    public boolean sendPlayer(String name, int clientID)  throws RemoteException{
        System.out.println("Name received");
        SetSpawnAnswer setSpawnAnswer = new SetSpawnAnswer(false);
        for(PlayerAbstract p:player){
            if(p.getName().equals(name) && !p.isConnected()){
                System.out.println("This player was disconnected before");
                if(p.getPosition() == null){
                    setSpawnAnswer = new SetSpawnAnswer(true);
                    p.setState(PlayerState.TOBESPAWNED);
                }
                else {
                    int damage = p.getPlayerBoard().getDamageTaken();
                    if ((damage > Constants.BETTERCOLLECTDAMAGE) && (damage <= Constants.BETTERSHOOTDAMAGE)) {
                        p.setState(PlayerState.BETTER_COLLECT);
                    } else if ((damage > Constants.BETTERSHOOTDAMAGE) && (damage <= Constants.DEATH_THRESHOLD)) {
                        p.setState(PlayerState.BETTER_SHOOT);
                    } else if ((p.getPosition().getRow() == -1000) && (p.getPosition().getCol() == -1000)) {
                        p.setState(PlayerState.TOBESPAWNED);
                        setSpawnAnswer = new SetSpawnAnswer(true); //at the very start all of them need to be spawned
                    } else
                        p.setState(PlayerState.NORMAL);
                }

                ((ConcretePlayer)p).setClientID(clientID);
                serverRMI.getGameManagerFromId(clientID).getController().getCurrentGame().getCurrentGameBoard().addPlayerBoard((ConcretePlayer) p);
                System.out.println("So his new clientID is: "+clientID);

                GameBoard currentGameBoard = serverRMI.getGameManagerFromId(clientID).getController().getCurrentGame().getCurrentGameBoard();
                GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(currentGameBoard);
                List<PlayerAbstract> playerInServer = serverRMI.getGameManagerFromId(clientID).getController().getPlayers();

                for(PlayerAbstract player:playerInServer){
                    if(player.getClientID() == clientID){
                        PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(player.getHand());
                        serverRMI.getGameManagerFromId(clientID).sendToSpecific(playerHandAnswer, clientID);
                    }
                }
                System.out.println("Sending the game board to the new client");
                serverRMI.getGameManagerFromId(clientID).sendToSpecific(gameBoardAnswer, clientID);
                serverRMI.getGameManagerFromId(clientID).sendToSpecific(setSpawnAnswer, clientID);
                return true;
            }
            else if(p.getName().equals(name) && p.isConnected()){
                name = name + clientID;
            }
        }
        System.out.println("Adding: "+name);
        if(player.size() == 5)
            return false;
        PlayerAbstract playerToAdd = new ConcretePlayer(name);
        playerToAdd.setState(PlayerState.TOBESPAWNED);
        playerToAdd.setCharacterChosen(false);
        ((ConcretePlayer) playerToAdd).setClientID(clientID);
        player.add(playerToAdd);
        return true;
    }

    @Override
    public boolean addPlayerCharacter(String name, int clientID) throws RemoteException{
        for(int i = 0; i < player.size(); i++){
            if(player.get(i).getClientID() == clientID){
                this.player.get(i).setPlayerCharacter(Figure.fromString(name));
                this.player.get(i).setCharacterChosen(true);
                this.serverRMI.getGameManagerFromId(clientID).addPlayer(player.get(i));
                this.serverRMI.getGameManagerFromId(clientID).getController().getCurrentGame().getCurrentGameBoard().addGameCharacter(new GameCharacter(Figure.fromString(name)));
            }
            System.out.println("For now I have received client number " +player.get(i).getClientID());
        }
        return true;
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
    public boolean sendInitialSkulls(int initialSkulls, int clientID) throws RemoteException{
        serverRMI.getGameManagerFromId(clientID).setInitialSkulls(initialSkulls);
        System.out.println("Initial skulls choice received");
        return true;
    }

    @Override
    public int getInitialSkulls(int clientID){
        return serverRMI.getGameManagerFromId(clientID).getInitialSkulls();
    }

    /*
    @Override
    public void startMatch() throws RemoteException{
        System.out.println("Starting the match");
        startGame = this.serverRMI.getServer().startMatch();
    }*/


    @Override
    public boolean sendMap(int numMap, int clientID)  throws RemoteException{
        System.out.println("Map choice received");
        this.numMap = numMap;
        serverRMI.getGameManagerFromId(clientID).setMap(numMap);
        serverRMI.getGameManagerFromId(clientID).createController();
        System.out.println("Controller set for serverRMI");
        return true;
    }

    @Override
    public boolean addMapPlayer(int clientID) throws RemoteException{
        serverRMI.addMapClient(getPlayer(clientID));
        System.out.println("Added the map of the client and of the player");
        return true;
    }

    @Override
    public List<PlayerAbstract> getPlayerList() throws RemoteException{
        return player;
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

