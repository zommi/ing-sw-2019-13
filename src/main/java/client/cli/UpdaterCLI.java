package client.cli;


import client.*;
import exceptions.CommandIsNotValidException;
import server.controller.playeraction.Action;
import server.controller.playeraction.ActionParser;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;  // Import the Scanner class
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdaterCLI  implements Updater,Runnable{


    private Connection connection;
    private boolean startGame = false;
    private boolean alwaysTrue = true;

    private GameModel gameModel;
    public UpdaterCLI(){
        super();
    }

    @Override
    public void update(Observable obs, Object object){
        if(object.equals("GameBoard")){
            System.out.println("New Update of the gameboard");
        }

        if(object.equals("Map")){
            System.out.println("New Update of the map");
        }

        if(object.equals("PlayerBoard")){
            System.out.println("New Update of the playerboard");
        }

        if(object.equals("PlayerHand")){
            System.out.println("New Update of the playerhand");
        }
    }

    @Override
    public void set() throws NotBoundException, RemoteException { //this method has to be run every time a new client starts. every cli needs to be an observer of the gameModel
        boolean hasChosen = false;
        String read;
        String playerName;
        String methodChosen;
        Boolean mapChosen = false;

        String mapName = "No one has chosen yet";

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        do{
            System.out.println(">Insert your player name:");
            playerName = myObj.nextLine();
        } while (playerName.equals(""));

        System.out.println("Name is: " +playerName);

        do {
            System.out.println(">Choose connection method:");
            System.out.println("(1) RMI");
            System.out.println("(2) Socket");
            methodChosen = myObj.nextLine();
            if (methodChosen.equals("1") || methodChosen.equals("2")) {
                hasChosen = true;
            }
        } while (!hasChosen);
        System.out.println("Method set: " +methodChosen);


        int lastClientID = GameModel.getNextClientID(); //because it is static. It will always return 0. Then the server will return the true clientID

        if (methodChosen.equals("1")) {
            connection = new ConnectionRMI(lastClientID);
            System.out.println("RMI connection was set up");
        } else{
            connection = new ConnectionSocket(lastClientID);
            System.out.println("Socket connection was set up");
        }
        gameModel = connection.getGameModel();  //because the gameModel is instantiated in the connection when it is started. this way both socket and RMI can read it
        gameModel.addObserver(this);
        try{
            connection.configure();
        }
        catch (RemoteException e) {
            System.out.println("Remote Exception caught");
            e.printStackTrace();
        }




        if(gameModel.getClientID() == 0) { //only if it is the first client!
            do {
                System.out.println(">Choose the map you want to use:");
                System.out.println("Piccola (1)");
                System.out.println("Media (2)");
                System.out.println("Grande (3)");
                System.out.println("Gigante (4)");
                read = myObj.nextLine();
                if (read.equals("1")) {
                    mapName = "map11.txt";
                    mapChosen = true;
                } else if (read.equals("2")) {
                    mapName = "map12.txt";
                    mapChosen = true;
                } else if (read.equals("3")) {
                    mapName = "map21.txt";
                    mapChosen = true;
                }
                else if (read.equals("4")) {
                    mapName = "map22.txt";
                    mapChosen = true;
                }
            } while (!mapChosen);
            startGame = true;
            System.out.println(">You have chosen the map: " +mapName);
        }
        else
        {
            mapName = connection.getMap();
            System.out.println(">Your friend has chosen the map: " +mapName);
        }


        int mapNumber;
        if (mapName.equals("map11.txt")) {
            mapNumber = 1;
        } else if (mapName.equals("map12.txt")) {
            mapNumber = 2;
        } else if (mapName.equals("map21.txt")) {
            mapNumber = 3;
        }
        else //(mapName.equals("map22.txt"))
            mapNumber = 4;
        connection.add(playerName, mapNumber);
    }


    @Override
    public void run(){
        String read;
        Action action;
        ActionParser actionParser = new ActionParser();
        Scanner myObj = new Scanner(System.in);
        try {
            this.set();
        }
        catch(NotBoundException|RemoteException nbe) {
            System.out.println("Exception caught");
        }
        while (alwaysTrue) {
            System.out.println(">Write a command: ");
            read = myObj.nextLine();
            if (startGame) {
                try{
                    action = actionParser.createValidEvent(read);
                    connection.send(action);
                } catch (CommandIsNotValidException e) {
                    System.out.println(">The command written is not valid");
                }
            } else {
                System.out.println("Match isn't started, please wait a minute");
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.submit(new UpdaterCLI());
    }
}
