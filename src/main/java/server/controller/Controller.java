package server.controller;

import client.*;
import client.powerups.PowerUpPack;
import client.weapons.ShootPack;
import constants.Color;
import constants.Constants;
import exceptions.WrongGameStateException;
import server.MyTimerTask;
import server.Server;
import server.controller.playeraction.*;
import server.controller.playeraction.normalaction.CollectAction;
import server.controller.playeraction.normalaction.MoveAction;
import server.controller.playeraction.normalaction.ShootAction;
import server.controller.turns.TurnHandler;
import server.controller.turns.TurnPhase;
import server.model.cards.AmmoTile;
import server.model.cards.WeaponCard;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.map.GameMap;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.ConcretePlayer;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerBoard;
import server.model.player.PlayerState;
import view.*;

import java.util.*;

public class Controller {

    private int currentID;

    private int grenadeID;

    private Game currentGame;

    private GameMap currentMap;

    private Server server;

    private List<SquareAbstract> squaresToUpdate;

    private List<PlayerAbstract> playersToSpawn;

    public Controller(int mapChoice, int initialSkulls, Server server){
        this.currentGame = new Game(mapChoice, initialSkulls);
        this.currentMap = this.currentGame.getCurrentGameMap();
        this.server = server;
        this.currentID = 0;
        this.grenadeID = -1;
        this.squaresToUpdate = new ArrayList<>();
        this.playersToSpawn = new ArrayList<>();
    }

    public WeaponCard drawWeapon(){
        return this.currentGame.drawWeapon();
    }

    public AmmoTile drawAmmo(){
        return this.currentGame.drawAmmo();
    }

    public int getCurrentID(){
        return this.currentID;
    }

    public int getGrenadeID(){
        return this.grenadeID;
    }

    public void nextCurrentID(){
        if(currentID == currentGame.getActivePlayers().size() - 1){
            currentID = 0;
        }
        else{
            this.currentID = currentID + 1;
        }
    }



    public String getCurrentCharacter(){
        for(PlayerAbstract playerAbstract : currentGame.getActivePlayers()){
            if(playerAbstract.getClientID() == currentID)
                //return playerAbstract.getName() + ", " + playerAbstract.getCharacterName();
                return playerAbstract.getCharacterName();
        }
        return "No one is playing";
    }

    public void makeAsynchronousAction(int clientID, Info action){
        ConcretePlayer player = (ConcretePlayer)currentGame.getActivePlayers().get(clientID);
        TurnHandler turnHandler = currentGame.getTurnHandler();

        if(action instanceof SpawnInfo){
            SpawnAction spawnAction = new SpawnAction((SpawnInfo) action, player, currentGame.getCurrentGameBoard());
            turnHandler.setAndDoSpawn(spawnAction);
            server.sendToEverybodyRMI(new GameBoardAnswer(currentGame.getCurrentGameBoard()));
            server.sendToSpecificRMI(new SetSpawnAnswer(false), clientID);
            server.sendToSpecificRMI(new PlayerHandAnswer(player.getHand()), clientID);
            this.playersToSpawn.remove(currentGame.getPlayerFromId(clientID));
            if(this.playersToSpawn.isEmpty()){
                turnHandler.nextPhase();
            }
        }

        if(action instanceof DrawInfo){
            DrawAction drawAction = new DrawAction(player, currentGame);
            turnHandler.setAndDoSpawn(drawAction);
            server.sendToSpecificRMI(new PlayerHandAnswer(player.getHand()), clientID);
            System.out.println("sending the playerhand to the clientID: " + clientID);
        }
    }

    public boolean makeAction(int clientID, Info action){
        TurnHandler turnHandler = currentGame.getTurnHandler();  //the phase depends on the action the player is sending!! it may be the first, the second or the third one
        turnHandler.setController(this);
        ConcretePlayer currentPlayer = (ConcretePlayer) currentGame.getCurrentPlayer();
        turnHandler.setCurrentPlayer(currentPlayer);

        System.out.println("We are in the game state: " +currentGame.getCurrentState());
        System.out.println("We are in the action number: " +turnHandler.getCurrentPhase());


        //TODO if(finalfrenzy)
        //{
        //    currentGame.nextState();
        //}


        if(currentGame.getCurrentGameBoard().getTrack().getRemainingSkulls() == 0){
            System.out.println("The game is finished, the winner is...");
            try{
                currentGame.nextState();
            }
            catch(WrongGameStateException e){
                e.printStackTrace();
            }
        }

        if ((currentPlayer.getPlayerState().equals(PlayerState.DISCONNECTED)) ||
                (currentPlayer.getPlayerState().equals(PlayerState.DEAD)) ||
                (currentGame.getCurrentState().equals(GameState.END_GAME))) {
            return false;
        }

        if(action instanceof PowerUpPack){
            PowerUpAction powerUpAction = new PowerUpAction((PowerUpPack) action,currentGame, currentPlayer);
            turnHandler.setAndDoAction(powerUpAction);
            this.sendCollectShootAnswersRMI(currentPlayer, currentID);
        }

        if(action instanceof DrawInfo){
            DrawAction drawAction = new DrawAction(currentPlayer, currentGame);
            turnHandler.setAndDoAction(drawAction);
            server.sendToSpecificRMI(new PlayerHandAnswer(currentPlayer.getHand()), currentID);
            System.out.println("sending the playerhand to the clientID: " +currentID);
        }

        if(action instanceof SpawnInfo){
            SpawnAction spawnAction = new SpawnAction((SpawnInfo) action, currentPlayer, currentGame.getCurrentGameBoard());
            turnHandler.setAndDoAction(spawnAction);
            server.sendToEverybodyRMI(new GameBoardAnswer(currentGame.getCurrentGameBoard()));
            server.sendToSpecificRMI(new SetSpawnAnswer(false), currentID);
            server.sendToSpecificRMI(new PlayerHandAnswer(currentPlayer.getHand()), currentID);
        }

        if(action instanceof MoveInfo){
            MoveAction moveAction = new MoveAction((MoveInfo) action, currentPlayer, currentMap);
            turnHandler.setAndDoAction(moveAction);
            //MapAnswer mapAnswer = new MapAnswer(this.currentGame.getCurrentGameMap());
            GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(this.currentGame.getCurrentGameBoard());
            //server.sendToEverybodyRMI(mapAnswer);
            server.sendToEverybodyRMI(gameBoardAnswer);
        }
        else if(action instanceof CollectInfo){
            MoveInfo temp = new MoveInfo(((CollectInfo)action).getRow(),((CollectInfo)action).getCol());
            CollectAction collectAction = new CollectAction(temp, (CollectInfo) action, currentPlayer, currentMap);
            turnHandler.setAndDoAction(collectAction);
            this.sendCollectShootAnswersRMI(currentPlayer, clientID);
        }
        else if(action instanceof ShootPack){
            ShootAction shootAction = new ShootAction((ShootPack) action, currentPlayer, currentGame); // TODO add player
            turnHandler.setAndDoAction(shootAction);
            sendCollectShootAnswersRMI(currentPlayer, clientID);
            //TODO check if the target has a powerup
            List<PlayerAbstract> listOfPlayers = currentGame.getActivePlayers();

            for(int i = 0; i < listOfPlayers.size(); i++){
                for(int j = 0; j < listOfPlayers.get(i).getHand().getPowerupHand().size(); j++){
                    if(listOfPlayers.get(i).getHand().getPowerupHand().get(j).getName().equals("Tagback Grenade")){
                        System.out.println("Found a player that has the tagback grenade");
                        if(listOfPlayers.get(i).getJustDamagedBy() != null){
                            grenadeID = listOfPlayers.get(i).getJustDamagedBy().getClientID();
                            TimerTask timerTask = new TimerTaskController();
                            Timer timer = new Timer(true);
                            timer.schedule(timerTask, 0);
                            System.out.println("Waiting for the other player to do the action");
                        }
                        grenadeID = -1;
                    }
                }
            }
        }


        if(turnHandler.getCurrentPhase() == TurnPhase.END_TURN){
            System.out.println("We are in the end turn");
            turnHandler.nextPhase();
            System.out.println("Turning next phase");
            this.nextCurrentID();
            System.out.println("Number of actions ended " +turnHandler.getCurrentPhase());
            //ChangeCurrentPlayerAnswer change = new ChangeCurrentPlayerAnswer();
            //server.sendToEverybodyRMI(change);
        }
        return true;
    }


    public void sendChangeCurrentPlayer(){
        ChangeCurrentPlayerAnswer changeAnswer = new ChangeCurrentPlayerAnswer();
        server.sendToEverybodyRMI(changeAnswer);
    }

    public void sendCollectShootAnswersRMI(ConcretePlayer player, int clientID){
        //MapAnswer mapAnswer = new MapAnswer(this.currentGame.getCurrentGameMap());
        GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(this.currentGame.getCurrentGameBoard());
        //PlayerBoardAnswer playerBoardAnswer = new PlayerBoardAnswer(player.getPlayerBoard());
        PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(player.getHand());
        //server.sendToEverybodyRMI(mapAnswer);
        server.sendToEverybodyRMI(gameBoardAnswer);
        //server.sendToEverybodyRMI(playerBoardAnswer);
        server.sendToSpecificRMI(playerHandAnswer, clientID);
    }

    public void sendSquaresRestored(){
        server.sendToEverybodyRMI(new GameBoardAnswer(this.currentGame.getCurrentGameBoard()));
    }

    public Game getCurrentGame(){
        return this.currentGame;
    }


    public void addClientInMap(PlayerAbstract player){
        try{
            ((ConcretePlayer)player).setCurrentGame(currentGame);
            this.currentGame.addPlayer(player);
        }
        catch(WrongGameStateException e){
            System.out.println("Wrong game state exception");
            e.printStackTrace();
        }
    }


    public List<PlayerAbstract> getPlayers(){
        return currentGame.getActivePlayers();
    }

    public void addSquareToUpdate(SquareAbstract square) {
        this.squaresToUpdate.add(square);
    }

    public void restoreSquares() {
        for(SquareAbstract square : this.squaresToUpdate){
            if(square instanceof Square){
                square.addItem(drawAmmo());
            } else {
                square.addItem(drawWeapon());
            }
        }
        squaresToUpdate.clear();
    }

    public boolean handleDeaths() {
        int skullsToAdd;
        boolean needToSpawn = false;
        PlayerBoard board;
        for(PlayerAbstract player : this.currentGame.getActivePlayers()){
            if(player.getPlayerBoard().getDamageTaken() > Constants.DEATH_THRESHOLD){
                player.die();
                skullsToAdd = player.isOverkilled() ? 2 : 1;
                this.getCurrentGame().getCurrentGameBoard().getTrack().removeSkull(skullsToAdd,player.getKillerColor());
                sendSpawnAnswer(player);
                if(player.isOverkilled()){
                    board = this.getCurrentGame().getCurrentGameBoard().getBoardFromColor(player.getKillerColor());
                    board.addMarks(1,player.getColor());
                }
                needToSpawn = true;
            }
        }
        return needToSpawn;
    }

    private void sendSpawnAnswer(PlayerAbstract playerAbstract) {
        this.playersToSpawn.add(playerAbstract);
        server.sendToSpecificRMI(new SetSpawnAnswer(true),playerAbstract.getClientID());
        server.sendToEverybodyRMI(new PlayerDiedAnswer());
        server.sendToEverybodyRMI(new GameBoardAnswer(this.currentGame.getCurrentGameBoard()));
    }

    public void sendSpawnRequest() {
        for(PlayerAbstract player : this.playersToSpawn){
            server.sendToSpecificRMI(new SpawnCommandAnswer(), player.getClientID());
        }
    }
}
