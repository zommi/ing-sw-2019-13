package client;

import constants.Constants;
import server.model.map.GameMap;
import server.model.player.Figure;
import view.ServerAnswer;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

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
    private String serverAddress;
    private static final int REGISTRATION_PORT = 1337;
    private int registrationPort;
    private String currentCharacter;

    public ConnectionSocket(int clientID){
        this.clientID = clientID;
        this.gameModel = new GameModel();
        this.socket = null;
        this.characterName = "No name yet";
        getProperties();
    }

    private void getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(Constants.PATH_TO_CONFIG));
            serverAddress = properties.getProperty("app.serverIp");
            registrationPort = Integer.valueOf(properties.getProperty("app.serverSocketPort"));
        } catch (IOException e) {
            System.out.println("File not found, given default values");
            e.printStackTrace();
            serverAddress = SERVER_ADDRESS;
            registrationPort = REGISTRATION_PORT;
        }
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }

    @Override
    public void setCurrentCharacter(String currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    public boolean getError(){
        return this.error;
    }

    public void setMapNum(int mapNum) {
        this.mapNum = mapNum;
    }

    public void setInitialSkulls(int initialSkulls) {
        this.initialSkulls = initialSkulls;
    }

    public int getClientID(){
        try {
            while (gameModel.getSetupRequestAnswer() == null)
                Thread.sleep(500);
        }catch(InterruptedException e){
            System.out.println("Interrupted exception");
        }
        return gameModel.getSetupRequestAnswer().getClientID();
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
    public void configure(String name){
        try {
            System.out.println("Configuring socket connection...");
            this.socket = new Socket(serverAddress, registrationPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            listenerSocketThread = new ListenerSocketThread(gameModel, socket, this);
            Thread thread = new Thread(listenerSocketThread);
            thread.start();

            //sending name
            send(new NameInfo(name));

        } catch (IOException e) {
            System.out.println("SOCKET CONFIGURATION FAILED");
            e.printStackTrace();
        }
    }

    public void send(Info info){
        gameModel.setJustDidMyTurn(true);
        try {
            outputStream.reset();
            outputStream.writeObject(info);
            outputStream.flush();
        } catch (IOException e) {
            System.console().printf("ERROR DURING SERIALIZATION\n");
            e.printStackTrace();
            gameModel.setJustDidMyTurn(false);
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
