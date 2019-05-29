package client;

import server.model.map.GameMap;
import server.model.player.Figure;
import view.ServerAnswer;

import java.io.*;
import java.net.Socket;

public class ConnectionSocket implements Connection {

    private Socket socket;
    private int clientID;
    private String mapChoice;
    private GameMap map;
    private GameModel gameModel;
    private int initialSkulls;
    private boolean isConnected;
    private int startGame = 0;
    private boolean error = false;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    private static final String SERVER_ADDRESS  = "localhost";
    private static final int REGISTRATION_PORT = 1337;

    public ConnectionSocket(int clientID){
        this.clientID = clientID;
        this.gameModel = new GameModel();
        this.isConnected = false;
        this.socket = null;
    }

    public boolean getError(){
        return this.error;
    }

    public void addPlayerCharacter(String name){
        send(new SetupInfo(clientID,name));
    }

    public void startTimer() {

    }

    public void saveAnswer(ServerAnswer answer) {

    }

    public boolean CharacterChoice(String name){
        return Figure.fromString(name) != null;
        //TODO controllare che non sia già preso magari mettendo una lista di character sul gamemodel
    }


    public GameMap getMap(){
        return this.map;
    }

    @Override
    public String getMapName() {
        return this.mapChoice;
    }

    public String getMapChoice() {
        return mapChoice;
    }

    public int getClientID(){
        return this.clientID;
    }

    public String getCurrentCharacter(){
        return null;
    }

    public int getCurrentID(){
        return 0;
    }

    @Override
    public int getStartGame(){
        return this.startGame;
    }

    public void sendConnection() {

    }

    public void configure(){
        try {
            System.out.println("Configuring socket connection...");
            this.gameModel.setClientID(this.clientID);
            this.socket = new Socket(SERVER_ADDRESS, REGISTRATION_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("SOCKET CONFIGURATION FAILED");
            e.printStackTrace();
        }
    }

    public void send(Info info){
        try {
            outputStream.writeObject(info);
            outputStream.flush();
        } catch (IOException e) {
            System.console().printf("ERROR DURING SERIALIZATION\n");
            e.printStackTrace();
        }
    }

    public void initializeSocket(){

    }

    public GameModel getGameModel(){
        return this.gameModel;
    }

    public void add(String playerName, int map, int initialSkulls){
        send(new SetupInfo(map,initialSkulls,playerName));
    }

    public int getMapIndexFromName(String name) {
        switch (name){
            case "map11.txt":
                return 0;
            case "map12.txt":
                return 1;
            case "map21.txt":
                return 2;
            case "map22.txt":
                return 3;
        }
        return  -1;
    }

    public int getInitialSkulls(){
        return this.initialSkulls;
    }

}
