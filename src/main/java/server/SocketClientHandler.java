package server;


import client.AsynchronousInfo;
import client.NameInfo;
import client.SetupInfo;
import client.Info;
import exceptions.WrongGameStateException;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerState;
import view.ServerAnswer;
import view.SetupAnswer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler implements Runnable {
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Integer clientID;
    private GameManager gameManager;
    private boolean firstPlayer;


    public SocketClientHandler(Socket socket, GameManager gameManager){
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.gameManager = gameManager;     //could be changed later
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
        if(info instanceof NameInfo){
            NameInfo nameInfo = (NameInfo) info;
            SetupAnswer setupAnswer = new SetupAnswer();

            clientID = gameManager.getServer().getIdFromName(nameInfo.getName());
            if(clientID == null){
                //player has never connected before
                clientID = gameManager.getServer().addClient();     //just gets the id
                Client client = new Client(clientID, gameManager);
                client.setSocketClientHandler(this);
                gameManager.getServer().addClient(client);          //adds client to the hashmap

                if(gameManager.isNoPlayer()){                       //decides skulls and map
                    gameManager.setNoPlayer(false);
                    setupAnswer.setFirstPlayer(true);
                    firstPlayer = true;
                }

                setupAnswer.setGameCharacter(true);

            }else{
                //player is reconnecting right now

                //setting real and old gameManager
                gameManager = gameManager.getServer().getGameManagerFromId(clientID);

                //reconnecting (with socket)
                gameManager.getServer().getClientFromId(clientID).setSocketClientHandler(this);
                gameManager.getController().getCurrentGame().getPlayerFromId(clientID).setConnected(true);
            }

            //sends confirm
            SocketInfo socketInfo = new SocketInfo();
            socketInfo.setServerAnswer(setupAnswer);
            socketInfo.setClientID(clientID);
            if(gameManager.isSetupComplete()){      //sends mapName and initialSkulls
                socketInfo.setMapNum(gameManager.getMapChoice());
                socketInfo.setInitialSkulls(gameManager.getInitialSkulls());
            }
            writeToStream(socketInfo);
        }

        else if(info instanceof SetupInfo){
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
                //now we can add the complete player to the list in gameManager, because its size will be
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
