package server;

import client.ReceiverInterface;
import constants.Constants;
import exceptions.WrongGameStateException;
import server.controller.Controller;
import server.model.game.Game;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerHand;
import view.*;

import java.rmi.RemoteException;
import java.util.*;

public class GameManager {

    private Game game;
    private int mapChoice;
    private Controller controller;
    private int idToDisconnect = -1;
    private int initialSkulls;
    private int lastClientIdAdded;
    private int startGame = 0;
    private List<Integer> listOfClients = new ArrayList<>();
    private List<PlayerAbstract> playerList = new ArrayList<>();
    private List<PlayerAbstract> playerDisconnectedList = new ArrayList<>();

    private boolean firstPlayerDesigned;

    private Server server;


    public GameManager(Server server){
        this.server = server;
        firstPlayerDesigned = false;
    }

    public synchronized boolean isFirstPlayerDesigned() {
        return firstPlayerDesigned;
    }

    public synchronized void setFirstPlayerDesigned(boolean firstPlayerDesigned) {
        this.firstPlayerDesigned = firstPlayerDesigned;
    }

    public Server getServer() {
        return server;
    }

    public int getStartGame(){
        return this.startGame;
    }

    public GameProxyInterface getGameProxy(){
        return server.getGameProxy();
    }

    public synchronized int getInitialSkulls() {
        return initialSkulls;
    }

    //posso far partire un thread del game passandogli playerList, controller, gameproxy
    public int startMatch(){ //TODO here we have a problem: what if the player does not choose the character in time?
        if(listOfClients.size() < Constants.MIN_PLAYERS){ //if after 30 seconds we have less than 3 players, the game does not start
            System.out.println("The game still has less than 3 players");
            listOfClients = null;
            game = null;
            startGame = 2;

            //informs socket clients
            sendToEverybody(null);

            return 2;
        }
        //now we have to start the game!
        else{
            game = controller.getCurrentGame();
            game.setPlayersNames();

            //adding active characters to the gameboard
            for(PlayerAbstract playerAbstract : game.getActivePlayers()){
                game.getCurrentGameBoard().getActiveCharacters().add(playerAbstract.getGameCharacter());
            }

            System.out.println("Created the game");
            System.out.println("Now I will send the map to the client");

            //send initial info
            InitialMapAnswer initialMapAnswer = new InitialMapAnswer(mapChoice);
            GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(controller.getCurrentGame().getCurrentGameBoard());
            SetSpawnAnswer setSpawnAnswer = new SetSpawnAnswer(true); //at the very start all of them need to be spawned

            sendToEverybody(initialMapAnswer);
            sendToEverybody(gameBoardAnswer);
            sendToEverybody(setSpawnAnswer);

            //send playerhands to every player
            for(PlayerAbstract playerAbstract : controller.getCurrentGame().getActivePlayers()){
                PlayerHand playerHand = controller.getCurrentGame().getPlayerFromId(playerAbstract.getClientID()).getHand();
                sendToSpecific(new PlayerHandAnswer(playerHand), playerAbstract.getClientID());
            }

            startGame = 1;

            //informs socket clients that startGame has changed
            sendToEverybody(null);

            System.out.println("The game is starting");
            try{
                game.nextState();
            }
            catch(WrongGameStateException e){
                e.printStackTrace();
            }
            System.out.println("We are in the game state: " +game.getCurrentState());
            return 1; //not useful
        }
    }

    public synchronized void addPlayer(PlayerAbstract player){
        playerList.add(player);
        if(playerList.size() == Constants.MIN_PLAYERS){ //start timer di N secondi
            TimerTask timerTask = new MyTimerTask(this);
            Timer timer = new Timer(true);
            timer.schedule(timerTask, 0);
            System.out.println("Task started");
        }
    }

    public void addClientsDisconnected(int id){
        this.listOfClients.add(id);
    }

    public synchronized Figure getFreeFigure(){

        //assuming maximum number of player has not been reached
        for(Figure figure : Figure.values()){
            if(!isCharacterTaken(figure.name()))
                return figure;
        }
        return null; //this should never happen
    }

    public synchronized boolean isCharacterTaken(String nameChar){
        System.out.println("Checking if the character is already taken by someone else");
        System.out.println("In my list I have " + playerList.size() +"players, i will check if they already have chosen their characters");
        for(PlayerAbstract playerAbstract : playerList){
            if( playerAbstract.isCharacterChosen() && playerAbstract.getCharacterName().equalsIgnoreCase(nameChar)){
                System.out.println("Found " + playerAbstract.getCharacterName());
                System.out.println("The character is already taken by someone else");
                return true;
            }
        }
        System.out.println("The character name you chose is ok");
        return false;
    }

    public void sendToSpecific(ServerAnswer serverAnswer, int clientID){
        server.getClientFromId(clientID).send(serverAnswer);
    }

    public void sendToEverybody(ServerAnswer serverAnswer){
        for(PlayerAbstract playerAbstract : game.getActivePlayers()){
            sendToSpecific(serverAnswer, playerAbstract.getClientID());
        }
    }

/*
        try {
            Map<Integer, ReceiverInterface> temp = getGameProxy().getClientRMIadded();
            System.out.println("Sending the update to: "+clientID);
            for (Map.Entry<Integer, ReceiverInterface> entry : temp.entrySet()) {
                ReceiverInterface value = entry.getValue();
                idToDisconnect = entry.getKey();  //when it catches the exception we know which id is the one
                if(value.getClientID() == clientID){
                    temp.get(idToDisconnect).publishMessage(serverAnswer);
                    System.out.println("Sent an update to a client");
                    break;
                }
            }
        }
        catch(RemoteException e){
            System.out.println("A client has been disconnected");
            try{
                for(int i = 0; i < listOfClients.size(); i++){
                    if(listOfClients.get(i) == idToDisconnect){
                        listOfClients.remove(i);
                    }
                }
                if(listOfClients.size() < 3){
                    //TODO termina la partita
                    System.out.println("The game is ended. We are now proceeding in proclaiming the winner");
                }
                else
                {
                    System.out.println("Disconnecting the player: " +idToDisconnect);
                    getGameProxy().getClientRMIadded().remove(idToDisconnect); //ELIMINATES THE CONNECTION FROM THE CONNECTION HASMAP
                    for(PlayerAbstract p:playerList){
                        if(p.getClientID() == idToDisconnect){   //ELIMINATES THE PLAYER FROM THE LIST IN SERVER
                            playerDisconnectedList.add(p);
                            p.setConnected(false);
                            System.out.println("Disconnected the player: " +idToDisconnect);
                        }
                    }
                    game.getCurrentGameBoard().getHashMap().remove(idToDisconnect);
                }
            }
            catch(RemoteException re){
                re.printStackTrace();
            }

        }
    }*/

    public int getLastClientIdAdded(){
        return lastClientIdAdded;
    }

    public Controller getController(){
        return controller;
    }

    public void setInitialSkulls(int initialSkulls){
        if(initialSkulls >= Constants.MIN_SKULLS && initialSkulls <= Constants.MAX_SKULLS)
            this.initialSkulls = initialSkulls;
        else
            this.initialSkulls = Constants.MIN_SKULLS;
    }

    public int getMapChoice() {
        return mapChoice;
    }

    public void setMapChoice(int mapChoice) {
        this.mapChoice = mapChoice;
    }

    public void setMap(int numMap){
        mapChoice = numMap;
    }

    public void createController(){
        System.out.println("Instantiating the controller");
        controller = new Controller(mapChoice, initialSkulls, this);
        System.out.println("Controller created");
    }
}
