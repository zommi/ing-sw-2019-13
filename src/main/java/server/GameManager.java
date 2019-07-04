package server;

import constants.Color;
import constants.Constants;
import server.controller.Controller;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;
import view.*;

import java.util.*;

public class GameManager {

    private Server server;
    private Controller controller;
    private Game game;

    private int mapChoice;
    private int initialSkulls;
    private boolean mapSkullsSet;

    private List<PlayerAbstract> activePlayers = new ArrayList<>();
    private boolean noPlayer;

    private int gameStarted;
    private boolean gameStarting;
    private boolean gameOver;

    private Timer timer;
    private TimerTask startTimerTask;



    public GameManager(Server server){
        this.server = server;
        gameStarted = 0;
        mapSkullsSet = false;
        noPlayer = true;
    }

    boolean isGameOver() {
        return gameOver;
    }

    public void addToActivePlayers(PlayerAbstract playerAbstract){
        activePlayers.add(playerAbstract);
    }

    void setInactive(PlayerAbstract playerAbstract){
        if(!activePlayers.contains(playerAbstract)){
            System.out.println(playerAbstract.getName() + " is already set as inactive!");
            return;
        }

        sendToSpecific(new DisconnectAnswer(), playerAbstract.getClientID());
        playerAbstract.setActive(false);
        activePlayers.remove(playerAbstract);
        System.out.println("Active players are now " + activePlayers.size());


        if(activePlayers.size() < Constants.MIN_PLAYERS_TO_CONTINUE)
            endGame();
        else
            System.out.println(playerAbstract.getName() + "'s turns will be skipped from now on");
    }

    public void setActive(PlayerAbstract playerAbstract){
        //cannot be called if game is not started, the player in fact will be excluded from the game in that case

        if(gameStarted == 0)
            return;

        if(!activePlayers.contains(playerAbstract)) {
            addToActivePlayers(playerAbstract);
            playerAbstract.setActive(true);

            String message = playerAbstract.getName() + " is not inactive anymore, he will play his turns again";
            System.out.println(message);
            sendEverybodyExcept(new MessageAnswer(message), playerAbstract.getClientID());
        }else{
            System.out.println(playerAbstract.getName() + " is already active!");
            return;
        }

        //client has to be unlocked
        sendToSpecific(new ReconnectAnswer(), playerAbstract.getClientID());
    }

    public void endGame(){
        game.getTurnHandler().getCurrentTimerTask().cancel();
        controller.distributeEndGamePoints();
        for(int i = 0; i<10; i++)
            System.out.println(Color.RED.getAnsi() + "GAME OVER" + Constants.ANSI_RESET);

        game.setCurrentState(GameState.GAME_OVER);
        Map<PlayerAbstract, Integer> playerToPoint = new HashMap<>();
        PlayerAbstract winner = game.getPlayers().get(0); //arbitrary
        int max = 0;
        for(PlayerAbstract playerAbstract : game.getPlayers()){
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

        //removing already disconnected clients from server database
        List<PlayerAbstract> clonedList = new ArrayList<>(game.getPlayers());
        for(PlayerAbstract playerAbstract : clonedList){
            if(!server.getClientFromId(playerAbstract.getClientID()).isConnected())
                server.removeClient(playerAbstract.getClientID());
        }

    }

    public List<PlayerAbstract> getActivePlayers() {
        return activePlayers;
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
        mapChoice = 1;
        setMapSkullsSet(true);
    }

    public Server getServer() {
        return server;
    }

    public int getGameStarted(){
        return this.gameStarted;
    }

    public void startMatch(){

        //timer has expired
        gameStarting = false;

        if(!mapSkullsSet){
            defaultSetup();
        }

        //now we can call createController because map and skulls have been set
        createController();

        System.out.println("Setting up game...");

        //adding players to the game
        for(PlayerAbstract playerAbstract : activePlayers){
            game.addPlayerToGame(playerAbstract);
        }

        //adding characters to the gameboard and setting the game into the players
        for(PlayerAbstract playerAbstract : game.getPlayers()){
            game.getCurrentGameBoard().getActiveCharacters().add(playerAbstract.getGameCharacter());
            playerAbstract.setCurrentGame(game);
        }

        //setting first client id as current player
        controller.setCurrentID(game.getPlayers().get(game.getCurrentPlayerIndex()).getClientID());

        //giving each player two cards
        for(PlayerAbstract playerAbstract : game.getPlayers()){
            for(int i = 1; i<=Constants.NUM_POWERUP_START; i++){
                playerAbstract.drawPowerupNoLimits();
            }
        }

        //moving to next phase
        game.nextState();

        System.out.println("All set up\nSending stuff to the clients...");

        //send initial info
        GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(controller.getCurrentGame().getCurrentGameBoard());
        SetSpawnAnswer setSpawnAnswer = new SetSpawnAnswer(true); //at the very start all of them need to be spawned

        sendToEverybody(gameBoardAnswer);
        sendToEverybody(setSpawnAnswer);

        //send playerhands to every player
        for(PlayerAbstract playerAbstract : controller.getCurrentGame().getPlayers()){
            sendToSpecific(new PlayerHandAnswer(playerAbstract.getHand()), playerAbstract.getClientID());
        }

        System.out.println("Stuff sent");

        //game is started
        gameStarted = 1;

        //informs socket clients that gameStarted has changed
        sendToEverybody(null);
    }

    public synchronized boolean addPlayerBeforeMatchStarts(PlayerAbstract player){
        if(gameStarted == 1)
            return false;

        if(activePlayers.size() == Constants.MAX_PLAYERS)
            return false;   //player is not added

        activePlayers.add(player);

        System.out.println("added " + player.getName());

        //starting timer
        if(activePlayers.size() == Constants.MIN_PLAYERS){
            startTimerTask = new GameStartTimer(this);
            timer = new Timer(true);
            timer.schedule(startTimerTask, Constants.GAME_START_TIMER_MSEC);
            gameStarting = true;
            System.out.println("GameStartTimer created at " + new Date());
        }

        //cancel the timer and start the match
        if(activePlayers.size() == Constants.MAX_PLAYERS){
            startTimerTask.cancel();
            server.setCurrentGameManager(new GameManager(server));
            this.startMatch();
        }

        return true;
    }

    public void removePlayerFromActivePlayers(String name) {

        //removing from "temp" activePlayers
        Iterator<PlayerAbstract> iterator = activePlayers.iterator();
        PlayerAbstract tempPlayer;
        while(iterator.hasNext()){
            tempPlayer = iterator.next();
            if(tempPlayer.getName().equalsIgnoreCase(name)) {
                iterator.remove();
            }
        }

        //canceling timer if already started
        if(activePlayers.size() < Constants.MIN_PLAYERS && gameStarting){
            //cancel timer
            System.out.println("Canceling timer...");
            startTimerTask.cancel();
            gameStarting = false;
            System.out.println("Timer canceled");
        }

        /*//removing actual player from real list in game, if the game is started
        if(gameStarted == 1)
            game.removePlayer(game.getPlayer(name));*/
    }

    public synchronized Figure getFreeFigure(){

        //assuming maximum number of player has not been reached
        for(Figure figure : Figure.values()){
            if(isCharacterFree(figure.name()))
                return figure;
        }
        return null; //this should never happen
    }

    public synchronized boolean isCharacterFree(String nameChar){
        System.out.println("Checking if the character is already taken by someone else");
        System.out.println("In my list I have " + activePlayers.size() +" players, i will check if they already have chosen their characters");
        for(PlayerAbstract playerAbstract : activePlayers){
            if( playerAbstract.isCharacterChosen() && playerAbstract.getCharacterName().equalsIgnoreCase(nameChar)){
                System.out.println("Found " + playerAbstract.getCharacterName());
                System.out.println("The character is already taken by someone else");
                return false;
            }
        }
        System.out.println("The character name you chose is ok");
        return true;
    }

    public void sendToSpecific(ServerAnswer serverAnswer, int clientID){
            if(server.getClientFromId(clientID) != null){
                server.getClientFromId(clientID).send(serverAnswer);
            }
    }

    public void sendToEverybody(ServerAnswer serverAnswer){
        if(game != null) {
            for (PlayerAbstract playerAbstract : game.getPlayers()) {
                sendToSpecific(serverAnswer, playerAbstract.getClientID());
            }
        }
    }

    public void sendEverybodyExcept(ServerAnswer serverAnswer, int clientId){
        if(game != null) {
            for (PlayerAbstract playerAbstract : game.getPlayers()) {
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
}
