package server;


import client.SetupInfo;
import client.Info;
import exceptions.WrongGameStateException;
import server.model.game.Game;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler extends Thread{
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Game game;


    public SocketClientHandler(Socket socket, Game game){
        try {
            this.clientSocket = socket;
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.game = game;
        } catch (IOException e) {
            System.console().printf("ERROR DURING INITIALIZATION PROCESS");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Info infoRead;
        while(true){
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

    public void processAction(Info info) throws WrongGameStateException {
        if(info instanceof SetupInfo){
            if(this.game == null) {
                this.game = new Game(((SetupInfo) info).getMapChoice(), ((SetupInfo) info).getInitialSkulls());
            }
            if(((SetupInfo) info).isCharacterSetup()){
                //dentro setupinfo c'è un metodo che prende un clientId e un personaggio
                this.game.getPlayerFromId(((SetupInfo) info).getClientId()).
                        setPlayerCharacter(Figure.fromString(((SetupInfo) info).getCharacterChosen()));
            } else{
                this.game.addPlayer(new ConcretePlayer(((SetupInfo) info).getPlayerName()));
            }

        }
    }


}
