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
    private GameManager gameManager;


    public SocketClientHandler(Socket socket, GameManager gameManager){
        try {
            this.clientSocket = socket;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.gameManager = gameManager;
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
        clientID = gameManager.getServer().addClient(gameManager);
        System.out.println("Thread started, added client " + clientID);
        socketInfo.setClientID(clientID);
        if(gameManager.isFirstPlayerDesigned()){      //sends mapName and initialSkulls
            socketInfo.setMapNum(gameManager.getMapChoice());
            socketInfo.setInitialSkulls(gameManager.getInitialSkulls());
        }
        writeToStream(socketInfo);

        //the following loop waits for client action
        while(true){ //NOSONAR
            try {
                infoRead = (Info) inputStream.readObject();
                processAction(infoRead);
            } catch (IOException e) {
                System.out.println("CLIENT HAS DISCONNECTED");
                //Disconnects player todo
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (WrongGameStateException e){
                System.out.println("ACTION NOT ALLOWED, PLAYER IS BANNED");
                break;
            }
        }
    }

    private void processAction(Info info) throws WrongGameStateException, IOException{
        if(info instanceof SetupInfo){
            SetupInfo setupInfo = (SetupInfo) info;
            if(gameManager.getController() == null) {
                gameManager.setInitialSkulls(setupInfo.getInitialSkulls());
                gameManager.setMapChoice(setupInfo.getMapChoice());

                //now we can call createController because map and skulls have been set
                gameManager.createController();

            }
            if(setupInfo.isCharacterSetup()){
                if(Figure.fromString(setupInfo.getCharacterChosen()) != null &&
                        !gameManager.isCharacterTaken(setupInfo.getCharacterChosen())){
                    //player in Game has already been created
                    gameManager.getController().getCurrentGame().getPlayerFromId(clientID).
                            setPlayerCharacter(Figure.fromString(setupInfo.getCharacterChosen()));
                    gameManager.getController().getCurrentGame().getPlayerFromId(clientID).setCharacterChosen(true);
                }
                else{
                    gameManager.getController().getCurrentGame().getPlayerFromId(clientID).
                            setPlayerCharacter(gameManager.getFreeFigure());
                    gameManager.getController().getCurrentGame().getPlayerFromId(clientID).setCharacterChosen(true);
                }
                //now we can add the complete player to the list in server, because its size will be
                //checked for starting the game
                gameManager.addPlayer(gameManager.getController().getCurrentGame().getPlayerFromId(clientID));

            } else{
                //adding a player to activePlayerList in Game
                //they are without their GameCharacter for now, it will be added later
                ConcretePlayer concretePlayer = new ConcretePlayer(setupInfo.getPlayerName());
                concretePlayer.setClientID(clientID);
                concretePlayer.setState(PlayerState.TOBESPAWNED);
                gameManager.getController().getCurrentGame().addPlayer(concretePlayer);
            }
            //sends an answer with updated data
            SocketInfo socketInfo = new SocketInfo();
            socketInfo.setStartGame(gameManager.getStartGame());
            writeToStream(socketInfo);

        }
        else if(info instanceof AsynchronousInfo){
            gameManager.getController().makeAsynchronousAction(clientID, ((AsynchronousInfo) info).getInfo());
        }
        else{
            gameManager.getController().makeAction(clientID, info);
        }
    }

    private synchronized void writeToStream(SocketInfo socketInfo){
        try {
            outputStream.reset();
            outputStream.writeObject(socketInfo);
            outputStream.flush();
        }catch(IOException e){
            //Disconnects client todo
        }

    }


    public void publishSocketMessage(ServerAnswer answer){
        //sends the answer to the client through the output stream, encapsulating the answer in a SocketInfo object
        //if the match is started, various IDs are added to the SocketInfo object to let the client know about the match status

        //this method will only be called by the controller's thread
        SocketInfo socketInfo = new SocketInfo();
        socketInfo.setServerAnswer(answer);
        socketInfo.setStartGame(gameManager.getStartGame());
        socketInfo.setCurrentID(gameManager.getController().getCurrentGame().getCurrentPlayer().getClientID());
        socketInfo.setGrenadeID(gameManager.getController().getGrenadeID());
        socketInfo.setCurrentCharacter(gameManager.getController().getCurrentGame().getCurrentPlayer().getCharacterName());
        writeToStream(socketInfo);

    }

    public int getClientID() {
        return clientID;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
