package client;

import server.model.map.GameMap;
import server.model.player.Figure;
import view.ServerAnswer;

import java.io.*;
import java.net.Socket;

public class ConnectionSocket implements Connection {

    private Socket socket;
    private int clientID;

    private int currentID;
    private int grenadeID;

    private int mapNum;
    private String mapChoice;

    private GameModel gameModel;
    private int initialSkulls;
    private int startGame = 0;
    private boolean error = false;

    private String characterName;

    private ObjectOutputStream outputStream;
    private ListenerSocketThread listenerSocketThread;

    private static final String SERVER_ADDRESS  = "localhost";
    private static final int REGISTRATION_PORT = 1337;
    private String currentCharacter;

    public ConnectionSocket(int clientID){
        this.clientID = clientID;
        this.gameModel = new GameModel();
        this.socket = null;
        this.characterName = "No name yet";
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }

    @Override
    public void setCurrentCharacter(String currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    @Override
    public void sendName(String name) {
        send(new NameInfo(name));
    }


    public boolean getError(){
        return this.error;
    }

    @Override
    public String getCharacterName(){
        return characterName;

    }

    public void addPlayerCharacter(String name){
        send(new SetupInfo(name));
    }

    public boolean isCharacterChosen(String name){
        return Figure.fromString(name) != null;
        //doesn't check if character is taken by another player, in that case it will be assigned random
    }

    public void setMapNum(int mapNum) {
        this.mapNum = mapNum;
    }

    public void setInitialSkulls(int initialSkulls) {
        this.initialSkulls = initialSkulls;
    }

    public String getMapName() {
        if(this.mapNum == 0)
            return "Map 1";
        else if(this.mapNum == 1)
            return "Map 2";
        else if(this.mapNum == 2)
            return "Map 3";
        else if(this.mapNum == 3)
            return "Map 4";
        else
            return "No one has chosen yet";
    }

    public int getClientID(){
        try {
            while (clientID == -1)
                Thread.sleep(500);
        }catch(InterruptedException e){
            System.out.println("Interrupted exception");
        }
        return this.clientID;
    }

    public String getCurrentCharacterName(){
        return currentCharacter;
    }

    @Override
    public int getCurrentID(){
        return currentID;
    }

    @Override
    public int getGrenadeID(){
        return grenadeID;
    }


    @Override
    public int getStartGame(){
        return this.startGame;
    }

    //must get client id from the server and set it
    public void configure(){
        try {
            System.out.println("Configuring socket connection...");
            this.socket = new Socket(SERVER_ADDRESS, REGISTRATION_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            listenerSocketThread = new ListenerSocketThread(gameModel, socket, this);
            Thread thread = new Thread(listenerSocketThread);
            thread.start();

        } catch (IOException e) {
            System.out.println("SOCKET CONFIGURATION FAILED");
            e.printStackTrace();
        }
    }

    public void send(Info info){
        try {
            outputStream.reset();
            outputStream.writeObject(info);
            outputStream.flush();
        } catch (IOException e) {
            System.console().printf("ERROR DURING SERIALIZATION\n");
            e.printStackTrace();
        }
    }

    @Override
    public void sendAsynchronous(Info action) {
        AsynchronousInfo asynchronousInfo = new AsynchronousInfo(action);
        send(asynchronousInfo);
    }

    public GameModel getGameModel(){
        return this.gameModel;
    }

    public void add(String playerName, int map, int initialSkulls){
        send(new SetupInfo(map,initialSkulls,playerName));
    }

    public int getInitialSkulls(){
        return this.initialSkulls;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setStartGame(int startGame) {
        this.startGame = startGame;
    }

    @Override
    public void setGrenadeID(int grenadeID) {
        this.grenadeID = grenadeID;
    }
}
