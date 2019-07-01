package client;

import server.SocketInfo;
import view.ChangeCurrentPlayerAnswer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
                //System.out.println("waiting for a SocketInfo");
                SocketInfo socketInfo = (SocketInfo) inputStream.readObject();
                processSocketInfo(socketInfo);
                //System.out.println("SocketInfo processed");


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
