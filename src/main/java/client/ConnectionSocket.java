package client;

import client.info.AsynchronousInfo;
import client.info.Connection;
import client.info.Info;
import client.info.NameInfo;
import constants.Constants;

import java.io.*;
import java.net.Socket;

public class ConnectionSocket implements Connection {

    private Socket socket;

    private int currentID;

    private GameModel gameModel;
    private int startGame = 0;
    private boolean error = false;


    private ObjectOutputStream outputStream;
    private ListenerSocketThread listenerSocketThread;

    private String serverAddress;
    private int registrationPort;
    private String currentCharacter;

    public ConnectionSocket() {
        this.gameModel = new GameModel();
        this.socket = null;
        serverAddress = Constants.SERVER_ADDRESS;
        registrationPort = Constants.SOCKET_PORT;
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

        gameModel.setGamemodelNotUpdated(true);

        try {
            outputStream.reset();
            outputStream.writeObject(info);
            outputStream.flush();
        } catch (IOException e) {
            System.console().printf("ERROR WHILE SENDING\n");
            e.printStackTrace();
            gameModel.setGamemodelNotUpdated(false);
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
