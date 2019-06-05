package client.cli;


import client.*;
import constants.Color;
import constants.Constants;
import exceptions.GameAlreadyStartedException;
import exceptions.NotEnoughPlayersException;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.map.SpawnPoint;
import view.PlayerBoardAnswer;
import view.PlayerHandAnswer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UpdaterCLI  implements Updater,Runnable{


    private Connection connection;
    private boolean alwaysTrue = true;

    private GameModel gameModel;

    private static final int MIN_SKULL = 5;
    private static final int MAX_SKULL = 8;

    private String name;
    private String characterName;

    public UpdaterCLI(){
        super();
    }


    @Override
    public void update(Observable obs, Object object){
        if(object.equals("GameBoard")){
            System.out.println("New Update of the gameboard");
        }

        if(object.equals("Change player")){
            System.out.println("Next player");
        }

        if(object.equals("Spawn")){
            System.out.println("New update of spawn");
        }

        if(object.equals("Weapons list")){
            System.out.println("New Update of the weapons");
        }

        if(object.equals("Map initialized")){
            System.out.println("New Update of the map number");
        }

        if(object.equals("Map")){
            System.out.println("New Update of the map");
            gameModel.getMap().getResult().printOnCLI();
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
            System.out.println(">Insert your name:");
            playerName = myObj.nextLine();
        } while (playerName.equals(""));

        this.name = playerName;
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
        gameModel = connection.getGameModel();
        //because the gameModel is instantiated in the connection when it is started.
        // this way both socket and RMI can read it
        gameModel.addObserver(this);
        connection.configure();
        //if(connection.getError() == true)
        //    throw new GameAlreadyStartedException();

        int mapNumber = 0;
        if(gameModel.getClientID() == 0) {//only if it is the first client!
            do{
                String stringChoice = "";
                int choice;
                System.out.println(">Choose the number of skulls (MIN: " +
                        MIN_SKULL + ", MAX: " + MAX_SKULL + "): ");
                try{
                    stringChoice = myObj.nextLine();
                    choice = Integer.parseInt(stringChoice);
                    if(choice < MIN_SKULL || choice > MAX_SKULL)
                        System.out.println("Please insert a valid number.");
                    else {
                        initialSkullsChosen = true;
                        initialSkulls = choice;
                    }
                }catch(NumberFormatException e){
                    System.out.println("Please insert a valid number.");
                }
            } while(!initialSkullsChosen);



            System.out.println(">You chose "+initialSkulls+" initial skulls!" );
            System.out.println(">Now choose the map you want to play with:" );
            String[] mapNames = {"Little", "Normal", "Big", "Huge"};
            List<String> mnList = Arrays.asList(mapNames);
            int choice;
            String stringChoice = "";
            do {
                for(int i = 0; i< mnList.size(); i++){
                    System.out.println(mnList.get(i) + " (" + (i+1) + ")");
                }
                try {
                    stringChoice =  myObj.nextLine();
                    choice = Integer.parseInt(stringChoice);
                    if(choice <mnList.size() && choice > 0){
                        mapChosen = true;
                        mapNumber = choice;
                    }
                    else{
                        System.out.println("Error: insert a valid number!");
                    }
                } catch(NumberFormatException e){
                    System.out.println("Error: insert a valid number");
                }

            } while (!mapChosen);
            System.out.println(">You have chosen the map: " +mapName);
        }
        else
        {
            do{
                mapName = connection.getMapName();
                initialSkulls = connection.getInitialSkulls();
            } while((mapName.equals("No one has chosen yet"))||(initialSkulls == 0));
            System.out.println(">Your friend has chosen the map: " +mapName);
            System.out.println(">Your friend has chosen the initial skulls number : " +initialSkulls);
        }

        connection.add(playerName, mapNumber, initialSkulls);

        String[] characterNames = {"SPROG", "VIOLET", "DESTRUCTOR", "DOZER", "BANSHEE"};
        List<String> cnList = Arrays.asList(characterNames);
        int choice;
        boolean set = false;
        String stringChoice;
        characterName = "";

        do{
            System.out.println(">Choose your character:");
            for(int i = 0; i< cnList.size(); i++){
                System.out.println(cnList.get(i) + " (" + (i+1) + ")");
            }
            try {
                stringChoice =  myObj.nextLine();
                choice = Integer.parseInt(stringChoice) - 1;
                if(choice < cnList.size() && choice >= 0){
                    set = true;
                    characterName = cnList.get(choice);
                    this.characterName = characterName;
                }
                else{
                    System.out.println("Error: insert a valid number!");
                }
            } catch(NumberFormatException e){
                System.out.println("Error: insert a valid number");
            }
        } while (!set || (!connection.CharacterChoice(characterName)));

        System.out.println("Name is: " +characterName.toUpperCase());
        connection.addPlayerCharacter(characterName); //TODO

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
        PlayerHandAnswer playerHand;
        PlayerBoardAnswer playerBoard;
        List<WeaponCard> weapons;
        List<PowerUpCard> powerups;
        int ammoRED;
        int ammoBLUE;
        int ammoYELLOW;
        try {
            this.set();
        }
        catch(NotBoundException|RemoteException|NotEnoughPlayersException|GameAlreadyStartedException nbe) {
            System.out.println("Exception caught");
            return;
        }
        ActionParser actionParser = new ActionParser(this);



        while (alwaysTrue) {
            System.out.println("entering the alwaysTrue cicle");
            if (connection.getStartGame() == 1) {
                actionParser.addGameModel(gameModel);

                //excluding my name from player names list and setting input
                List<String> pNames = new ArrayList<>();
                for(String string : gameModel.getPlayersNames())
                    if(!string.equals(name))
                        pNames.add(string);
                actionParser.setPlayersNames(pNames);

                System.out.println("Testing if the start game works: " +connection.getStartGame());
                if ((connection.getClientID() == connection.getCurrentID()) && (connection.getGrenadeID() == -1)) {
                    System.out.println("Testing what client I am in: i am in client: " +connection.getClientID() + "and the current id is: " +connection.getCurrentID());
                    playerHand = gameModel.getPlayerHand();
                    playerBoard = gameModel.getPlayerBoard(connection.getClientID());
                    weapons = playerHand.getWeaponHand();
                    System.out.println(">You have the following weapons: ");
                    if ((weapons == null) || (weapons.size() == 0)) {
                        System.out.println(">You have no weapons!");
                    } else {
                        for (int i = 0; i < weapons.size(); i++) {
                            System.out.println("> " + weapons.get(i).getName());
                        }
                    }
                    powerups = playerHand.getPowerupHand();
                    System.out.println(">You have the following powerups: ");
                    if ((powerups == null) || (powerups.size() == 0)) {
                        System.out.println(">You have no powerups!");
                    } else {
                        for (int i = 0; i < powerups.size(); i++) {
                            System.out.println("> " + powerups.get(i).getName());
                        }
                    }

                    if (playerBoard != null) { //these checks are used when the clients still do not have any update, at the start of the match
                        ammoRED = playerBoard.getRedAmmo();
                        System.out.println(">Red ammos:" + ammoRED);

                        ammoBLUE = playerBoard.getBlueAmmo();
                        System.out.println(">Blue ammos:" + ammoBLUE);

                        ammoYELLOW = playerBoard.getYellowAmmo();
                        System.out.println(">Yellow ammos:" + ammoYELLOW);
                    }
                    if(gameModel.getToSpawn()){
                        powerups = this.spawn(powerups, playerHand, actionParser);
                    }
                    this.startInput(actionParser, powerups);
                }
                else if(connection.getGrenadeID() != -1){
                    if(connection.getClientID() != connection.getGrenadeID()){
                        System.out.println("There is somebody that has a tagback grenade. Checking if he wants to use it");
                    }
                    else { //we are exactly in the player that has the current turn to use the tagback
                        Scanner myObj = new Scanner(System.in);
                        System.out.println("You have been shot. You can use the tagback grenade to give him 1 mark. Do you want to use it?");
                        System.out.println("Yes (1)");
                        System.out.println("No (2)");
                        String read = myObj.nextLine();
                        if(Integer.parseInt(read) == 1){
                            actionParser.createPowerUpEvent("Tagback Grenade");
                        }
                    }
                }
                else{
                    System.out.println("For now it is not your turn: " + connection.getCurrentCharacter() +"is playing");
                    try{
                        TimeUnit.SECONDS.sleep(5);
                    }
                    catch(InterruptedException e){
                        System.out.println("Exception while waiting");
                    }
                }
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



    public List<PowerUpCard> spawn(List<PowerUpCard> powerups, PlayerHandAnswer playerHand, ActionParser actionParser){
        String read;
        Scanner myObj = new Scanner(System.in);
        System.out.println("Creating the action of drawing");
        DrawInfo action = new DrawInfo();
        System.out.println("Sending the action of spawning to the server");
        connection.send(action);//TODO is it istant as an action?
        playerHand = gameModel.getPlayerHand();
        powerups = playerHand.getPowerupHand();
        System.out.println(">You have the following powerups: ");
        while ((powerups == null) || (powerups.isEmpty())) {
            System.out.println(">You have no powerups. There's a problem as you should have draw them");
            playerHand = gameModel.getPlayerHand();
            powerups = playerHand.getPowerupHand();
            try{
                TimeUnit.SECONDS.sleep(5);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        for (int i = 0; i < powerups.size(); i++) {
            System.out.println("> " + powerups.get(i).getName() +" (" +i +")");
        }
        System.out.println(">Choose one of the two powerups you have draw and discard it. You will be put in the spawn point of the color of that card : ");
        read = myObj.nextLine();
        while(Integer.parseInt(read) >= 2){
            System.out.println(">Choose one of the two powerups you have draw and discard it. You will be put in the spawn point of the color of that card : ");
            read = myObj.nextLine();
        }
        Info action1 = actionParser.createSpawEvent(powerups.get(Integer.parseInt(read)));
        System.out.println(">Sending your choice to the server: ");
        connection.send(action1);
        System.out.println("Ok you were put in the map, right in the spawn point of color: " +powerups.get(Integer.parseInt(read)).getColor());
        return powerups;
    }





    public void startInput(ActionParser actionParser, List<PowerUpCard> powerups){
        String read;
        int coordinatex = 0;
        int coordinatey = 0;
        Scanner myObj = new Scanner(System.in);
        int collectDecision = 0;
        boolean collectChosen = false;
        boolean powerupChosen = false;
        printPlayerBoard(gameModel.getPlayerBoard(gameModel.getClientID()));
        System.out.println(">Write a command: ");
        read = myObj.nextLine();
        int result = -1;
        if (read.toUpperCase().equals("MOVE")) {
            System.out.println(">You are in the position: row " +gameModel.getPlayerBoard(gameModel.getClientID()).getRow() + " col " +gameModel.getPlayerBoard(gameModel.getClientID()).getCol());
            System.out.println(">Choose the row you want to move to: ");
            coordinatex = Integer.parseInt(myObj.nextLine());
            System.out.println(">Choose the col you want to move to: ");
            coordinatey = Integer.parseInt(myObj.nextLine());
            Info action = actionParser.createMoveEvent(coordinatex, coordinatey);
            connection.send(action);
        } else if (read.toUpperCase().equals("SHOOT")) {
            System.out.println(">Choose the name of the weapon: ");
            String weaponChosen = myObj.nextLine();
            Info action = actionParser.createShootEvent(weaponChosen);
            connection.send(action);
        } else if (read.toUpperCase().equals("COLLECT")) {
            do {
                System.out.println(">Choose what you want to collect: ");
                System.out.println("Weapon Card (1)"); //1 is to collect weapon
                System.out.println("Ammo (2)"); //3 is to collect ammo
                read = myObj.nextLine();
                if (read.equals("1")) {
                    collectDecision = 1;
                    collectChosen = true;
                } else if (read.equals("2")) {
                    collectDecision = 2;
                    collectChosen = true;
                }
            } while (!collectChosen);
            do {
                System.out.println(">Do you want to move before collecting? ");
                System.out.println("Yes (1)"); //1 move
                System.out.println("No (2)"); //2 not move
                result = Integer.parseInt(myObj.nextLine());
            } while ((result != 1) && (result != 2));
            coordinatex = gameModel.getPlayerBoard(gameModel.getClientID()).getRow();
            coordinatey = gameModel.getPlayerBoard(gameModel.getClientID()).getCol();
            if(result == 1){
                System.out.println(">Choose the row you want to move to: ");
                coordinatex = Integer.parseInt(myObj.nextLine());
                System.out.println(">Choose the col you want to move to: ");
                coordinatey = Integer.parseInt(myObj.nextLine());
            }


            if(collectDecision == 1){ //he has chosen to collect a weapon
                do {
                    if(gameModel.getMap().getResult().getSquare(coordinatex, coordinatey) instanceof SpawnPoint)
                    {
                        System.out.println(">Choose which weapon you want to collect ");
                        for(int i = 0; i < ((SpawnPoint)gameModel.getMap().getResult().getSquare(coordinatex, coordinatey)).getWeaponCards().size(); i++) { //1 is to collect weapon
                            System.out.println("" +i +" " +((SpawnPoint)gameModel.getMap().getResult().getSquare(coordinatex, coordinatey)).getWeaponCards().get(i)); //1 is to collect weapon
                        }
                        result = Integer.parseInt(myObj.nextLine());
                        Info action = actionParser.createCollectEvent(coordinatex, coordinatey, result);
                        connection.send(action);
                    }
                    else{
                        System.out.println("This is not a spawn point, you can't collect weapons");
                        break;
                    }
                } while ((result != 0) && (result != 1) && (result != 2));
            }
            else if(collectDecision == 2) {
                Info action = actionParser.createCollectEvent(coordinatex, coordinatey, Constants.NO_CHOICE);
                connection.send(action);
            }
        }
        else if (read.toUpperCase().equals("USE POWERUP")) {
            do {
                System.out.println(">Choose what powerup you want to use: ");
                read = myObj.nextLine();
            }
            while ((read.equals("")) || ((!read.toUpperCase().equals("TELEPORTER")) && (!read.toUpperCase().equals("NEWTON")) && (!read.toUpperCase().equals("TARGETING SCOPE")) && (!read.toUpperCase().equals("TAGBACK GRANADE"))));
            Info action = actionParser.createPowerUpEvent(read.toUpperCase());
            connection.send(action);
        } else {
            System.out.println(">You have to choose between 'COLLECT', 'SHOOT', 'MOVE' and 'USE POWERUP' ");
            startInput(actionParser, powerups);
        }
    }

    public void printPlayerBoard(PlayerBoardAnswer p){
        System.out.println("You currently have: " +p.getResult().getMarksOfAColor(Color.RED) +" red marks tokens");
        System.out.println("You currently have: " +p.getResult().getMarksOfAColor(Color.BLUE) +" blue marks tokens");
        System.out.println("You currently have: " +p.getResult().getMarksOfAColor(Color.YELLOW) +" yellow marks tokens");
        System.out.println("You currently have: " +p.getResult().getDamageTaken() +" damage tokens");
        for(int i = 0; (i < p.getResult().getDamage().length) && (p.getResult().getDamage()[i] != null); i++){
            System.out.println("You currently have a " +(p.getResult().getDamage())[i].toString() +" damage token");
        }


    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.submit(new UpdaterCLI());
        //exec.shutdown();
    }
}
