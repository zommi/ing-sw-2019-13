package server;


import client.AsynchronousInfo;
import client.SetupInfo;
import client.Info;
import exceptions.WrongGameStateException;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerState;
import view.ServerAnswer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private int clientID;
    private Server server;


    public SocketClientHandler(Socket socket, Server server){
        try {
            this.clientSocket = socket;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.server = server;
        } catch (IOException e) {
            System.console().printf("ERROR DURING INITIALIZATION PROCESS");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Info infoRead;

        //sends clientID
        SocketInfo socketInfo = new SocketInfo();
        clientID = server.addClient();
        System.out.println("Thread started, added client " + clientID);
        socketInfo.setClientID(clientID);
        if(clientID != 0){      //sends mapName and initialSkulls
            socketInfo.setMapNum(server.getMapChoice());
            socketInfo.setInitialSkulls(server.getInitialSkulls());
        }
        writeToStream(socketInfo);

        //the following loop waits for client action
        while(true){ //NOSONAR
            try {
                infoRead = (Info) inputStream.readObject();
                processAction(infoRead);
            } catch (IOException e) {
                System.out.println("CLIENT HAS DISCONNECTED");
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (WrongGameStateException e){
                System.out.println("ACTION NOT ALLOWED, PLAYER IS BANNED");
                break;
            }
        }
    }

    private void processAction(Info info) throws WrongGameStateException {
        if(info instanceof SetupInfo){
            SetupInfo setupInfo = (SetupInfo) info;
            if(server.getController() == null) {
                server.setInitialSkulls(setupInfo.getInitialSkulls());
                server.setMapChoice(setupInfo.getMapChoice());

                //now we can call createController because map and skulls have been set
                server.createController();

            }
            if(setupInfo.isCharacterSetup()){
                if(Figure.fromString(setupInfo.getCharacterChosen()) != null && !server.isCharacterTaken(setupInfo.getCharacterChosen())){
                    //player in Game has already been created
                    server.getController().getCurrentGame().getPlayerFromId(clientID).
                            setPlayerCharacter(Figure.fromString(setupInfo.getCharacterChosen()));
                }
                else{
                    server.getController().getCurrentGame().getPlayerFromId(clientID).
                            setPlayerCharacter(server.getFreeFigure());
                }
                //now we can add the complete player to the list in server, because its size will be
                //checked for starting the game
                server.addPlayer(server.getController().getCurrentGame().getPlayerFromId(clientID));

            } else{
                //adding a player to activePlayerList in Game
                //they are without their GameCharacter for now, it will be added later
                ConcretePlayer concretePlayer = new ConcretePlayer(setupInfo.getPlayerName());
                concretePlayer.setClientID(clientID);
                concretePlayer.setState(PlayerState.TOBESPAWNED);
                server.getController().getCurrentGame().addPlayer(concretePlayer);
            }
            //sends an answer with updated data
            SocketInfo socketInfo = new SocketInfo();
            socketInfo.setStartGame(server.getStartGame());
            writeToStream(socketInfo);

        }
        else if(info instanceof AsynchronousInfo){
            server.getController().makeAsynchronousAction(clientID, ((AsynchronousInfo) info).getInfo());
        }
        else{
            server.getController().makeAction(clientID, info);
        }
    }

    private synchronized void writeToStream(SocketInfo socketInfo){
        try {
            outputStream.reset();
            outputStream.writeObject(socketInfo);
            outputStream.flush();
        }catch(IOException e){
            System.out.println("Error in writing to stream");
        }
    }


    public void publishSocketMessage(ServerAnswer answer) {
        //sends the answer to the client through the output stream, encapsulating the answer in a SocketInfo object
        //if the match is started, various IDs are added to the SocketInfo object to let the client know about the match status

        //this method will only be called by the controller's thread
        SocketInfo socketInfo = new SocketInfo();
        socketInfo.setServerAnswer(answer);
        socketInfo.setStartGame(server.getStartGame());
        socketInfo.setCurrentID(server.getController().getCurrentGame().getCurrentPlayer().getClientID());
        socketInfo.setGrenadeID(server.getController().getGrenadeID());
        socketInfo.setCurrentCharacter(server.getController().getCurrentGame().getCurrentPlayer().getCharacterName());
        writeToStream(socketInfo);

    }

    public int getClientID() {
        return clientID;
    }
}
