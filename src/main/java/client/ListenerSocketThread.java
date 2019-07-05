package client;

import answers.SocketInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Thread that listens to the server and reads the answers, processing them.
 * @author Matteo Pacciani
 */
public class ListenerSocketThread implements Runnable{

    private GameModel gameModel;
    private Socket socket;
    private Connection connectionSocket;

    private ObjectInputStream inputStream;

    public ListenerSocketThread(GameModel gameModel, Socket socket, Connection connectionSocket){
        this.gameModel = gameModel;
        this.socket = socket;
        this.connectionSocket = connectionSocket;
    }

    /**
     * Waits for the server to send an update, and processes it. If the server goes offline,
     * the game model is updated so that the client will be able to know it
     */
    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Got input stream");
        }catch(IOException e){
            System.out.println("Error while getting inputStream");
            return;
        }

        while(true){ //NOSONAR
            try {
                SocketInfo socketInfo = (SocketInfo) inputStream.readObject();
                processSocketInfo(socketInfo);

            }catch (IOException e) {
                System.out.println("Error while reading input");
                gameModel.setServerOffline(true);
                return;
            } catch (ClassNotFoundException e) {
                System.out.println("Error while reading input, class not found exception");
                return;
            }
        }
    }

    /**
     * Process the info reading the current data of the match, like the current player and so on
     * @param socketInfo the info sent by the server
     */
    private void processSocketInfo(SocketInfo socketInfo){

        if(socketInfo.getStartGame() != -1)
            connectionSocket.setStartGame(socketInfo.getStartGame());

        if(socketInfo.getCurrentID() != -1)
            connectionSocket.setCurrentID(socketInfo.getCurrentID());
        if(socketInfo.getCurrentCharacter() != null)
            connectionSocket.setCurrentCharacter(socketInfo.getCurrentCharacter());

        if(socketInfo.getServerAnswer() != null)
            gameModel.saveAnswer(socketInfo.getServerAnswer());

    }
}
