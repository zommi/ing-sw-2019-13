package server;

import constants.Color;
import constants.Constants;
import server.controller.Controller;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;
import answers.*;
import server.timer.GameStartTimer;

import java.util.*;

/**
 * This class manages a single game. When the game starts, a new game manager is created, to have multiple matches running at the
 * same time. This class is also responsible for communicating with the players of this particular match.
 * @author Matteo Pacciani
 */
public class GameManager {

    /**
     * A reference to the {@link Server} that is the same for all the created game managers
     */
    private Server server;
    private Controller controller;
    private Game game;

    private int mapChoice;
    private int initialSkulls;
    private boolean mapSkullsSet;

    /**
     * This list contains the players waiting for the match to start, when the game is not started yet.
     * When the game starts, this list keeps track of all the active players, removing those who disconnect
     * because of inactivity
     */
    private List<PlayerAbstract> activePlayers = new ArrayList<>();
    /**
     * True if there are no players in this particular game manager
     */
    private boolean noPlayer;

    /**
     * Equals 1 if the game is started, 0 otherwise
     */
    private int gameStarted;
    /**
     * True if the timer is running but has not been triggered yet
     */
    private boolean gameStarting;
    /**
     * True if the game is over
     */
    private boolean gameOver;

    private Timer timer;
    /**
     * The timer task responsible for starting the match
     */
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

    /**
     * Sets the player as inactive after the inactivity timer has been triggered
     * If the players are below the minimum required to continue the match,
     * the game is ended. The turns of the passed player won't be played until he does not
     * reconnect sending a {@link ReconnectAnswer}.
     * @param playerAbstract the player to set as inactive
     */
    public void setInactive(PlayerAbstract playerAbstract){
        if(!activePlayers.contains(playerAbstract)){
            System.out.println(playerAbstract.getName() + " is already set as inactive!");
            return;
        }

        if(server.getClientFromId(playerAbstract.getClientID()).isConnected())
            sendToSpecific(new DisconnectAnswer(), playerAbstract.getClientID());

        playerAbstract.setActive(false);
        activePlayers.remove(playerAbstract);
        System.out.println("Active players are now " + activePlayers.size());

        if(activePlayers.size() < Constants.MIN_PLAYERS_TO_CONTINUE && !gameOver)
            endGame();
        else
            System.out.println(playerAbstract.getName() + "'s turns will be skipped from now on");
    }

    /**
     * Called when a {@link ReconnectAnswer} is sent by the client that was previously disconnected.
     * Sets the passed player as active, so that his turns will be played again
     * @param playerAbstract the player to set as active
     */
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

    /**
     * Ends the game, distributing the last points and sending {@link GameOverAnswer}
     * to all the players of this game
     */
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

    /**
     * Called if the designed first player disconnects before sending the setup
     * Skulls and maps are decided arbitrarily
     */
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

    /**
     * Called by the trigger {@link GameStartTimer}. Starts the match setting up the first turn
     * and sending updates to the connected clients
     */
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

    /**
     * Adds a player to the starting game. If the minimum number of players is reached,
     * the {@link GameStartTimer} is started.
     * @param player the player to add to the match
     * @return true if the player has been added successfully
     */
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

    /**
     * Removes a player from the active players list
     * Cancels the timer if the number of players goes below the minimum to start the match
     * @param name the name of the player to remove
     */
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

    /**
     * Gets a figure that no one has chosen yet
     * @return the first free figure
     */
    public synchronized Figure getFreeFigure(){

        //assuming maximum number of player has not been reached
        for(Figure figure : Figure.values()){
            if(isCharacterFree(figure.name()))
                return figure;
        }
        return null; //this should never happen
    }

    /**
     * Checks if the selected figure is free or not
     * @param nameChar the name of the figure to check
     * @return true if the selected figure is free
     */
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


    /**
     * Sends an answer to a specific client
     * @param serverAnswer the answer to send
     * @param clientID the id of the client to send the answer to
     */
    public void sendToSpecific(ServerAnswer serverAnswer, int clientID){
            if(server.getClientFromId(clientID) != null){
                server.getClientFromId(clientID).send(serverAnswer);
            }
    }

    /**
     * Sends an answer to all the clients of this game
     * @param serverAnswer the answer to send
     */
    public void sendToEverybody(ServerAnswer serverAnswer){
        if(game != null) {
            for (PlayerAbstract playerAbstract : game.getPlayers()) {
                sendToSpecific(serverAnswer, playerAbstract.getClientID());
            }
        }
    }

    /**
     * Sends an answer to all the players of this game, except the selected client
     * @param serverAnswer the answer to send
     * @param clientId the client who won't receive the answer
     */
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

    /**
     * Sets up the {@link Game}, passing the map and the initial skulls
     */
    public void createController(){
        System.out.println("Instantiating the controller");
        controller = new Controller(mapChoice, initialSkulls, this);
        System.out.println("Controller created");
        game = controller.getCurrentGame();
    }
}
