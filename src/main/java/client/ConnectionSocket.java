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

    private int currentID;

    private GameModel gameModel;
    private int startGame = 0;
    private boolean error = false;


    private ObjectOutputStream outputStream;
    private ListenerSocketThread listenerSocketThread;

    private static final String SERVER_ADDRESS = "localhost";
    private String serverAddress;
    private static final int REGISTRATION_PORT = 1337;
    private int registrationPort;
    private String currentCharacter;

    public ConnectionSocket() {
        this.gameModel = new GameModel();
        this.socket = null;
        getProperties();
    }

    private void getProperties() {
        Properties properties = new Properties();
        try {

            FileInputStream configFile = new FileInputStream(Constants.PATH_TO_CONFIG);
            properties.load(configFile);
            configFile.close();

            serverAddress = properties.getProperty("app.serverIp");
            registrationPort = Integer.valueOf(properties.getProperty("app.serverSocketPort"));
        } catch (IOException e) {
            System.out.println("File not found, given default values");
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

    public boolean getError() {
        return this.error;
    }

    public int getClientID() {
        try {
            while (gameModel.getSetupRequestAnswer() == null)
                Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception");
        }
        return gameModel.getSetupRequestAnswer().getClientID();
    }

    public String getCurrentCharacterName() {
        return currentCharacter;
    }

    @Override
    public int getCurrentID() {
        return currentID;
    }

    @Override
    public int getStartGame() {
        return this.startGame;
    }

    //must get client id from the server and set it
    public void configure(String name) {
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

    public void send(Info info) {
        if (gameModel.isGameOver())
            return;

        gameModel.setJustDidMyTurn(true);

        try {
            outputStream.reset();
            outputStream.writeObject(info);
            outputStream.flush();
        } catch (IOException e) {
            System.console().printf("ERROR WHILE SENDING\n");
            e.printStackTrace();
            gameModel.setJustDidMyTurn(false);
        }
    }

    @Override
    public void sendAsynchronous(Info action) {
        send(new AsynchronousInfo(action));
    }

    public GameModel getGameModel() {
        return this.gameModel;
    }

    public void setStartGame(int startGame) {
        this.startGame = startGame;
    }
}
