package server;


import client.AsynchronousInfo;
import client.NameInfo;
import client.SetupInfo;
import client.Info;
import view.ServerAnswer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler implements Runnable {
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Integer clientID;
    private boolean firstPlayer;

    private Server server;


    public SocketClientHandler(Socket socket, Server server){
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.server = server;
            clientID = -1;
        } catch (IOException e) {
            System.console().printf("ERROR DURING INITIALIZATION PROCESS");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        //the following loop waits for client action
        //the first thing required is the name
        while(true){ //NOSONAR
            try {
                Info infoRead = (Info) inputStream.readObject();
                processAction(infoRead);
            } catch (IOException e) {
                System.out.println("CLIENT HAS DISCONNECTED");
                try{
                    if(clientID != -1)
                        server.getClientFromId(clientID).disconnect();
                }catch(NullPointerException npe){
                    //do nothing
                }
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void processAction(Info info) {
        if(info instanceof NameInfo){
            NameInfo nameInfo = (NameInfo) info;

            clientID = server.addConnection(nameInfo.getName(), "socket",
                    this, null);        //automatically sends setupAnswer
        }

        else if(info instanceof SetupInfo){
            SetupInfo setupInfo = (SetupInfo) info;
            server.setupPlayer(clientID, setupInfo);
        }
        else if(info instanceof AsynchronousInfo){
            server.getGameManagerFromId(clientID).getController()
                    .makeAsynchronousAction(clientID, ((AsynchronousInfo) info).getInfo());
        }
        else{
            server.getGameManagerFromId(clientID).getController().makeAction(clientID, info);
        }
    }

    private synchronized void writeToStream(SocketInfo socketInfo){
        try {
            outputStream.reset();
            outputStream.writeObject(socketInfo);
            outputStream.flush();
        }catch(IOException e){
            server.getClientFromId(clientID).disconnect();
        }

    }


    public void publishSocketMessage(ServerAnswer answer){
        //sends the answer to the client through the output stream, encapsulating the answer in a SocketInfo object
        //if the match is started, various IDs are added to the SocketInfo object to let the client know about the match status

        //this method will only be called by the controller's thread
        SocketInfo socketInfo = new SocketInfo();
        socketInfo.setServerAnswer(answer);

        try{
            GameManager gameManager = server.getGameManagerFromId(clientID);

            socketInfo.setStartGame(gameManager.getStartGame());
            socketInfo.setCurrentID(gameManager.getController().getCurrentGame().getCurrentPlayer().getClientID());
            socketInfo.setGrenadeID(gameManager.getController().getGrenadeID());
            socketInfo.setCurrentCharacter(gameManager.getController().getCurrentGame().getCurrentPlayer().getCharacterName());
        }catch(NullPointerException e){
            //dont't send
        }
        writeToStream(socketInfo);

    }

    public int getClientID() {
        return clientID;
    }
}
