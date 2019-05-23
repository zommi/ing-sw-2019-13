package client.cli;


import client.*;
import exceptions.GameAlreadyStartedException;
import exceptions.NotEnoughPlayersException;
import server.model.cards.PowerupCard;
import server.model.cards.WeaponCard;
import server.model.player.PlayerBoard;
import server.model.player.PlayerHand;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;  // Import the Scanner class
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UpdaterCLI  implements Updater,Runnable{


    private Connection connection;
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

        if(object.equals("Map initialized")){
            System.out.println("New Update of the map number");
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
    public void set() throws NotBoundException, RemoteException, NotEnoughPlayersException, GameAlreadyStartedException { //this method has to be run every time a new client starts. every cli needs to be an observer of the gameModel
        boolean hasChosen = false;
        String read;
        String playerName;
        String methodChosen;
        boolean mapChosen = false;
        int initialSkulls = -1;
        boolean initialSkullsChosen = false;

        String characterName;
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


        int lastClientID = 0;

        if (methodChosen.equals("1")) {
            connection = new ConnectionRMI(lastClientID);
            System.out.println("RMI connection was set up");
        } else{
            connection = new ConnectionSocket(lastClientID);
            System.out.println("Socket connection was set up");
        }
        gameModel = connection.getGameModel();  //because the gameModel is instantiated in the connection when it is started. this way both socket and RMI can read it
        gameModel.addObserver(this);
        connection.configure();
        if(connection.getError() == true)
            throw new GameAlreadyStartedException();


        if(gameModel.getClientID() == 0) {//only if it is the first client!
            do{
                System.out.println(">Choose the number of initial skulls you want to put on the killshot track: ");
                System.out.println("5 (1)");
                System.out.println("6 (2)");
                System.out.println("7 (3)");
                System.out.println("8 (4)");
                read = myObj.nextLine();
                if (read.equals("1")) {
                    initialSkulls = 5;
                    initialSkullsChosen = true;
                } else if (read.equals("2")) {
                    initialSkulls = 6;
                    initialSkullsChosen = true;
                } else if (read.equals("3")) {
                    initialSkulls = 7;
                    initialSkullsChosen = true;
                }
                else if (read.equals("4")) {
                    initialSkulls = 8;
                    initialSkullsChosen = true;
                }
                else {
                    System.out.println(">HEY! You have to choose between the options given!" );
                    initialSkulls = 0;
                }
            } while(!initialSkullsChosen);

            System.out.println(">You have chosen to use: "+initialSkulls+" initial skulls" );
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
                else{
                    System.out.println(">HEY! You have to choose between the options given!" );
                    mapName = "No one has chosen yet";
                }
            } while (!mapChosen);
            System.out.println(">You have chosen the map: " +mapName);
        }
        else
        {
            do{
                mapName = connection.getMap();
                initialSkulls = connection.getInitialSkulls();
            } while((mapName.equals("No one has chosen yet"))||(initialSkulls == 0));
            System.out.println(">Your friend has chosen the map: " +mapName);
            System.out.println(">Your friend has chosen the initial skulls number : " +initialSkulls);
        }


        int mapNumber = 0;
        if (mapName.equals("map11.txt")) {
            mapNumber = 1;
        } else if (mapName.equals("map12.txt")) {
            mapNumber = 2;
        } else if (mapName.equals("map21.txt")) {
            mapNumber = 3;
        }
        else if(mapName.equals("map22.txt")){
            mapNumber = 4;
        }
        connection.add(playerName, mapNumber, initialSkulls);


        do{
            System.out.println(">Insert your character name:");
            characterName = myObj.nextLine();
        } while ((characterName.equals("")) || (!connection.CharacterChoice(characterName)));

        System.out.println("Name is: " +characterName.toUpperCase());
        connection.addPlayerCharacter(characterName);

        /*if(gameModel.getClientID() == 0){
            System.out.println("Waiting 30 seconds for the others to join the game");
            startTimer();
            System.out.println("Waited 30 seconds for the others to join the game");
            connection.startMatch();
        }*/

        if(connection.getStartGame() == 2){
            System.out.println("Unfortunately, not enough people joined the game so you will be disconnected. Bye");
            throw new NotEnoughPlayersException();
        }
    }

    /*public void startTimer(){
        try{
            TimeUnit.SECONDS.sleep(30);
        }
        catch(InterruptedException e)
        {
            System.out.println("Exception thrown");
        }
        System.out.println("I waited 30 seconds");
    }*/


    @Override
    public void run(){
        String read;
        PlayerHand playerHand;
        PlayerBoard playerBoard;
        List<WeaponCard> weapons;
        List<PowerupCard> powerups;
        int collectDecision = 0;
        boolean collectChosen = false;
        boolean powerupChosen = false;
        int ammoRED;
        int ammoBLUE;
        int ammoYELLOW;
        int coordinatex;
        int coordinatey;
        Scanner myObj = new Scanner(System.in);
        try {
            this.set();
        }
        catch(NotBoundException|RemoteException|NotEnoughPlayersException|GameAlreadyStartedException nbe) {
            System.out.println("Exception caught");
            return;
        }

        while (alwaysTrue) {
            if (connection.getStartGame() == 1) { //the game can start
                //try{
                playerHand = gameModel.getPlayerHand();
                playerBoard = gameModel.getPlayerBoard();
                weapons = (ArrayList<WeaponCard>) playerHand.getWeapons();
                System.out.println(">You have the following weapons: ");
                if((weapons == null)||(weapons.size() == 0)){
                    System.out.println(">You have no weapons!");
                }
                else{
                    for(int i = 0; i < weapons.size(); i++){
                        System.out.println("> " +weapons.get(i).getName());
                    }
                }

                powerups = (ArrayList<PowerupCard>) playerHand.getPowerups();
                System.out.println(">You have the following puwerups: ");
                if((powerups == null)||(powerups.size() == 0)){
                    System.out.println(">You have no powerups!");
                }
                else {
                    for (int i = 0; i < powerups.size(); i++) {
                        System.out.println("> " + powerups.get(i).getName());
                    }
                }

                if(playerBoard != null){ //these checks are used when the clients still do not have any update, at the start of the match
                    ammoRED = playerBoard.getRedAmmo();
                    System.out.println(">You have %d red ammos:" +ammoRED);

                    ammoBLUE = playerBoard.getBlueAmmo();
                    System.out.println(">You have %d blue ammos:" +ammoBLUE );

                    ammoYELLOW = playerBoard.getYellowAmmo();
                    System.out.println(">You have %d yellow ammos:" +ammoYELLOW );
                }

                System.out.println(">Write a command: ");
                read = myObj.nextLine();
                if(read.toUpperCase().equals("MOVE")){
                    System.out.println(">Choose the coordinate x you want to move to: " );
                    coordinatex = Integer.parseInt(myObj.nextLine());
                    System.out.println(">Choose the coordinate y you want to move to: " );
                    coordinatey = Integer.parseInt(myObj.nextLine());
                    Info action = ActionParser.createMoveEvent(coordinatex, coordinatey);
                    connection.send(action);
                }
                else if(read.toUpperCase().equals("SHOOT")){
                    Info action = ActionParser.createShootEvent();
                    connection.send(action);
                }
                else if(read.toUpperCase().equals("COLLECT")){
                    do {System.out.println(">Choose what you want to collect: " );
                        System.out.println("Weapon Card (1)"); //1 is to collect weapon
                        System.out.println("PowerUp Card (2)"); //2 is to collect powerup
                        System.out.println("Ammo (3)"); //3 is to collect ammo
                        read = myObj.nextLine();
                        if (read.equals("1")) {
                            collectDecision = 1;
                            collectChosen = true;
                        } else if (read.equals("2")) {
                            collectDecision = 2;
                            collectChosen = true;
                        } else if (read.equals("3")) {
                            collectDecision = 3;
                            collectChosen = true;
                        }
                    } while (!collectChosen);
                    Info action = ActionParser.createCollectEvent(collectDecision);
                    connection.send(action);
                }
                else if(read.toUpperCase().equals("USE POWERUP")){
                    do {System.out.println(">Choose what powerup you want to use: " );
                        read = myObj.nextLine();
                    } while ((read.equals(""))||((!read.toUpperCase().equals("TELEPORTER"))&&(!read.toUpperCase().equals("NEWTON"))&&(!read.toUpperCase().equals("TARGETING SCOPE"))&&(!read.toUpperCase().equals("TAGBACK GRANADE"))));
                    Info action = ActionParser.createPowerUpEvent(read.toUpperCase());
                    connection.send(action);
                }
                else{
                    System.out.println(">You have to choose between 'COLLECT', 'SHOOT', 'MOVE' and 'USE POWERUP' ");
                }
                //}
                //catch (CommandIsNotValidException e) {
                //    System.out.println(">The command written is not valid");
                //}
            }
            else if(connection.getStartGame() == 0) {
                System.out.println("Match isn't started, please wait a minute");
                try{
                    TimeUnit.SECONDS.sleep(5);
                }
                catch(InterruptedException e){
                    System.out.println("Exception while waiting");
                }
            }
            else if(connection.getStartGame() == 2) {
                System.out.println("The number of players is not enough. Bye!");
                return;
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.submit(new UpdaterCLI());
        //exec.shutdown();
    }
}
