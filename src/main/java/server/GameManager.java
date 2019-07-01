package server;

import constants.Color;
import constants.Constants;
import exceptions.WrongGameStateException;
import server.controller.Controller;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;
import view.*;

import java.util.*;

public class GameManager {

    private Game game;
    private int mapChoice;
    private Controller controller;
    private int initialSkulls;
    private int startGame = 0;
    private List<Integer> listOfClients = new ArrayList<>();
    private List<PlayerAbstract> playerList = new ArrayList<>();
    private boolean noPlayer;
    private boolean mapSkullsSet;
    private boolean maxPlayersReached;

    private int activePlayersNum;

    private Server server;

    private boolean gameOver;


    public GameManager(Server server){
        this.server = server;
        mapSkullsSet = false;
        noPlayer = true;
        maxPlayersReached = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void disconnect(PlayerAbstract playerAbstract){
        if(!playerAbstract.isConnected()){
            System.out.println(playerAbstract.getName() + " is already disconnected!");
            return;
        }
        sendToSpecific(new DisconnectAnswer(), playerAbstract.getClientID());
        playerAbstract.setConnected(false);
        if(startGame == 1) {
            activePlayersNum--;
        }

        System.out.println(playerAbstract.getName() + "'s turns will be skipped from now on");
    }

    public void reconnect(PlayerAbstract playerAbstract){

        if(playerAbstract.isConnected()){
            System.out.println(playerAbstract.getName() + " is already active!");
            return;
        }

        playerAbstract.setConnected(true);

        if(startGame == 1)
            activePlayersNum++;

        String message = playerAbstract.getName() + " is not inactive anymore, he will play his turns again";
        System.out.println(message);
        sendEverybodyExcept(new MessageAnswer(message), playerAbstract.getClientID());


        //client has to be unlocked
        sendToSpecific(new ReconnectAnswer(), playerAbstract.getClientID());
    }

    public void endGame(){
        controller.distributeEndGamePoints();
        for(int i = 0; i<10; i++)
            System.out.println(Color.RED.getAnsi() + "GAME OVER" + Constants.ANSI_RESET);

        game.setCurrentState(GameState.GAME_OVER);
        Map<PlayerAbstract, Integer> playerToPoint = new HashMap<>();
        PlayerAbstract winner = null;
        int max = 0;
        for(PlayerAbstract playerAbstract : game.getActivePlayers()){
            playerToPoint.put(playerAbstract, playerAbstract.getPoints());
            if(playerAbstract.getPoints() > max){
                max = playerAbstract.getPoints();
                winner = playerAbstract;
            }
        }
        String message = winner.getName() + " HAS WON WITH " + winner.getPoints() + " POINTS";
        System.out.println(message);
        sendToEverybody(new MessageAnswer(message));
        sendToEverybody(new GameOverAnswer(playerToPoint, winner));
        gameOver = true;

    }

    public int getActivePlayersNum() {
        return activePlayersNum;
    }

    public GameState getGameState(){
        return controller.getCurrentGame().getCurrentState();
    }

    public synchronized boolean isMapSkullsSet() {
        return mapSkullsSet;
    }

    public synchronized void setMapSkullsSet(boolean mapSkullsSet) {
        this.mapSkullsSet = mapSkullsSet;
    }

    public synchronized boolean isNoPlayer() {
        return noPlayer;
    }

    public synchronized void setNoPlayer(boolean noPlayer) {
        this.noPlayer = noPlayer;
    }

    public void defaultSetup(){
        initialSkulls = Constants.MIN_SKULLS;
        mapChoice = 0;
        setMapSkullsSet(true);
    }

    public Server getServer() {
        return server;
    }

    public int getStartGame(){
        return this.startGame;
    }

    public synchronized int getInitialSkulls() {
        return initialSkulls;
    }

    //posso far partire un thread del game passandogli playerList, controller, gameproxy
    public int startMatch(){
        if(playerList.size() < Constants.MIN_PLAYERS){ //if after 30 seconds we have less than 3 players, the game does not start

            //todo throws null pointer exception when calling sendtoeverybody, change also listOfClients
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
            game.setPlayersNames();

            for(PlayerAbstract playerAbstract : playerList){
                game.addPlayer(playerAbstract);
            }

            activePlayersNum = game.getActivePlayers().size();

            //adding active characters to the gameboard
            for(PlayerAbstract playerAbstract : game.getActivePlayers()){
                game.getCurrentGameBoard().getActiveCharacters().add(playerAbstract.getGameCharacter());
            }

            //setting first client id
            controller.setCurrentID(game.getActivePlayers().get(game.getCurrentPlayerIndex()).getClientID());

            //giving each player two cards
            for(PlayerAbstract playerAbstract : game.getActivePlayers()){
                for(int i = 1; i<=Constants.NUM_POWERUP_START; i++){
                    playerAbstract.drawPowerupNoLimits();
                }
            }

            //moving to next phase
            System.out.println("Moving to NORMAL GameState");
            game.nextState();
            System.out.println("We are in the game state: " +game.getCurrentState());

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
                sendToSpecific(new PlayerHandAnswer(playerAbstract.getHand()), playerAbstract.getClientID());
            }

            startGame = 1;  //game is started

            //informs socket clients that startGame has changed
            sendToEverybody(null);


            return 1; //not useful
        }
    }

    public synchronized boolean addPlayer(PlayerAbstract player){
        if(maxPlayersReached)
            return false;   //player is not added
        System.out.println("added " + player.getName());
        playerList.add(player);
        player.setCurrentGame(game);
        if(playerList.size() == Constants.MIN_PLAYERS){ //start timer di N secondi
            TimerTask timerTask = new GameStartTimer(this);
            Timer timer = new Timer(true);
            timer.schedule(timerTask, Constants.GAME_START_TIMER_MSEC);
            System.out.println("GameStartTimer created at " + new Date());
        }
        else if(playerList.size() == Constants.MAX_PLAYERS)
            maxPlayersReached = true;     //no more players will be added

        return true;
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
            if(server.getClientFromId(clientID) != null){
                server.getClientFromId(clientID).send(serverAnswer);
            }
    }

    public void sendToEverybody(ServerAnswer serverAnswer){
        if(game != null) {
            for (PlayerAbstract playerAbstract : game.getActivePlayers()) {
                sendToSpecific(serverAnswer, playerAbstract.getClientID());
            }
        }
    }

    public void sendEverybodyExcept(ServerAnswer serverAnswer, int clientId){
        if(game != null) {
            for (PlayerAbstract playerAbstract : game.getActivePlayers()) {
                if(playerAbstract.getClientID() != clientId)
                    sendToSpecific(serverAnswer, playerAbstract.getClientID());
            }
        }
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
        if(mapChoice >= Constants.FIRST_MAP && mapChoice <= Constants.LAST_MAP)
            this.mapChoice = mapChoice;
        else
            this.mapChoice = Constants.FIRST_MAP;
    }

    public void setMap(int numMap){
        mapChoice = numMap;
    }

    public void createController(){
        System.out.println("Instantiating the controller");
        controller = new Controller(mapChoice, initialSkulls, this);
        System.out.println("Controller created");
        game = controller.getCurrentGame();
    }

    public void removePlayer(String name) {
        game.removePlayer(game.getPlayer(name));
    }
}
