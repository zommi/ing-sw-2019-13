package server.controller;

import client.*;
import client.powerups.PowerUpPack;
import client.weapons.ShootPack;
import constants.Color;
import constants.Constants;
import exceptions.WrongGameStateException;
import server.GameManager;
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

import java.rmi.RemoteException;
import java.util.*;

public class Controller {

    private int currentID;

    private int grenadeID;

    private Game currentGame;

    private GameMap currentMap;

    private boolean clientHasChosen;

    private GameManager gameManager;

    private List<SquareAbstract> squaresToUpdate;

    private List<PlayerAbstract> playersToSpawn;

    public Controller(int mapChoice, int initialSkulls, GameManager gameManager){
        this.currentGame = new Game(mapChoice, initialSkulls, this);
        this.currentMap = this.currentGame.getCurrentGameMap();
        this.gameManager = gameManager;
        this.currentID = 0;
        this.grenadeID = -1;
        this.squaresToUpdate = new ArrayList<>();
        this.clientHasChosen = false;
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

    public GameManager getGameManager() {
        return gameManager;
    }

    /*public void nextCurrentID(){
        if(currentID == currentGame.getActivePlayers().size() - 1){
            currentID = 0;
        }
        else{
            this.currentID = currentID + 1;
        }
    }*/



    public String getCurrentCharacterName(){
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
            if(!turnHandler.setAndDoSpawn(spawnAction)){
                return;
            }
            gameManager.sendToEverybody(new GameBoardAnswer(currentGame.getCurrentGameBoard()));
            gameManager.sendToSpecific(new SetSpawnAnswer(false), clientID);
            gameManager.sendToSpecific(new PlayerHandAnswer(player.getHand()), clientID);
            this.playersToSpawn.remove(currentGame.getPlayerFromId(clientID));
            if(this.playersToSpawn.isEmpty()){
                turnHandler.nextPhase();
            }
        }

        if(action instanceof DrawInfo){
            DrawAction drawAction = new DrawAction(player, currentGame);
            turnHandler.setAndDoSpawn(drawAction);
            gameManager.sendToSpecific(new PlayerHandAnswer(player.getHand()), clientID);
            System.out.println("sending the playerhand to the clientID: " + clientID);
        }
    }

    public boolean makeAction(int clientID, Info action){
        TurnHandler turnHandler = currentGame.getTurnHandler();  //the phase depends on the action the player is sending!! it may be the first, the second or the third one
        ConcretePlayer currentPlayer = (ConcretePlayer) currentGame.getCurrentPlayer();

        System.out.println("We are in the game state: " +currentGame.getCurrentState());
        System.out.println("We are in the action number: " +turnHandler.getCurrentPhase());


        //TODO if(finalfrenzy)
        //{
        //    currentGame.nextState();
        //}

        if (!currentPlayer.isConnected() ||
            currentID != clientID ||
            currentPlayer.getPlayerState().equals(PlayerState.DEAD)){
            sendErrorMessage(clientID, "It's not that easy to cheat here, try again!");
                return false;
        }

        if(currentGame.getCurrentState().equals(GameState.GAME_OVER)){
            sendErrorMessage(clientID, "You cannot do that now, game is over!");
            return false;
        }

        if(turnHandler.getCurrentPhase().equals(TurnPhase.POWERUP_TURN) && !(action instanceof PowerUpPack) &&
                !(action instanceof ReloadInfo)){
            sendErrorMessage(clientID);
            return false;
        }

        /*if(currentGame.getCurrentGameBoard().getTrack().getRemainingSkulls() == 0){
            try{
                currentGame.nextState();
            }
            catch(WrongGameStateException e){
                e.printStackTrace();
            }
        }*/

        //checks and executes actions

        boolean actionOk;

        if(action instanceof ReloadInfo){

            //checks that it's not the wrong time to reload
            if(turnHandler.getCurrentPhase() != TurnPhase.END_TURN) {
                sendErrorMessage(clientID);
                return false;
            }

            ReloadAction reloadAction = new ReloadAction((ReloadInfo) action, currentPlayer);
            actionOk = turnHandler.setAndDoAction(reloadAction);
            if(actionOk){
                gameManager.sendToSpecific(new PlayerHandAnswer(currentPlayer.getHand()), currentPlayer.getClientID());
                gameManager.sendToEverybody(new GameBoardAnswer(currentGame.getCurrentGameBoard()));
                //turnHandler.nextPhase();
            }else{
                sendErrorMessage(clientID);
            }

        }

        else if(action instanceof PowerUpPack){
            PowerUpAction powerUpAction = new PowerUpAction((PowerUpPack) action,currentGame, currentPlayer);
            if(turnHandler.setAndDoAction(powerUpAction))
                sendCollectShootAnswersRMI(currentPlayer, currentID);
            else
                sendErrorMessage(clientID);
        }

        else if(action instanceof DrawInfo){
            DrawAction drawAction = new DrawAction(currentPlayer, currentGame);
            if(turnHandler.setAndDoAction(drawAction)) {
                PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(currentPlayer.getHand());
                gameManager.sendToSpecific(playerHandAnswer, currentID);
                System.out.println("sending the playerhand to the clientID: " + currentID);
            }else{
                sendErrorMessage(clientID);
            }
        }

        else if(action instanceof SpawnInfo){
            SpawnAction spawnAction = new SpawnAction((SpawnInfo) action, currentPlayer, currentGame.getCurrentGameBoard());
            if(turnHandler.setAndDoAction(spawnAction)) {
                gameManager.sendToEverybody(new GameBoardAnswer(currentGame.getCurrentGameBoard()));
                gameManager.sendToSpecific(new SetSpawnAnswer(false), currentID);
                gameManager.sendToSpecific(new PlayerHandAnswer(currentPlayer.getHand()), currentID);
            }else{
                sendErrorMessage(clientID);
            }
        }

        else if(action instanceof MoveInfo){
            MoveAction moveAction = new MoveAction((MoveInfo) action, currentPlayer, currentMap);
            if(turnHandler.setAndDoAction(moveAction)) {
                GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(this.currentGame.getCurrentGameBoard());
                gameManager.sendToEverybody(gameBoardAnswer);
            }else{
                sendErrorMessage(clientID);
            }
        }

        else if(action instanceof CollectInfo){
            MoveInfo temp = new MoveInfo(((CollectInfo)action).getRow(),((CollectInfo)action).getCol());
            CollectAction collectAction = new CollectAction(temp, (CollectInfo) action, currentPlayer, currentMap);
            if(turnHandler.setAndDoAction(collectAction))
                this.sendCollectShootAnswersRMI(currentPlayer, clientID);
            else
                sendErrorMessage(clientID);
        }

        else if(action instanceof ShootPack) {
            ShootAction shootAction = new ShootAction((ShootPack) action, currentPlayer, currentGame); // TODO add player
            if(turnHandler.setAndDoAction(shootAction)) {
                sendCollectShootAnswersRMI(currentPlayer, clientID);
                //TODO check if the target has a powerup
                List<PlayerAbstract> listOfPlayers = currentGame.getActivePlayers();


                for (int i = 0; i < listOfPlayers.size(); i++) {
                    if(listOfPlayers.get(i).isConnected()) {
                        int j = 0;
                        while (j < listOfPlayers.get(i).getHand().getPowerupHand().size()) {
                            if (listOfPlayers.get(i).getHand().getPowerupHand().get(j).getName().equals("Tagback Grenade")) {
                                System.out.println("Found a player that has the tagback grenade");
                                if (listOfPlayers.get(i).getJustDamagedBy() != null) {
                                    if (listOfPlayers.get(i).getJustDamagedBy().equals(currentPlayer)) {
                                        grenadeID = listOfPlayers.get(i).getClientID();
                                        int IDShooter = grenadeID;
                                        GrenadeAnswer grenadeChanged = new GrenadeAnswer(grenadeID);
                                        gameManager.sendToEverybody(grenadeChanged);
                                        sendGrenadeAnswer();
                                        while (!clientHasChosen) { //TODO with socket it will be different! we are going to check if the client has sent me the action or not
                                            System.out.println("Waiting for the other player to do the action");
                                            try {
                                                System.out.println(" " + IDShooter);
                                                clientHasChosen = gameManager.getGameProxy().askClient(IDShooter);
                                                System.out.println("Client has chosen: " + clientHasChosen);
                                                if (clientHasChosen) {
                                                    System.out.println("test");
                                                    List<Info> actionGrenade = gameManager.getGameProxy().getGrenadeAction(IDShooter);
                                                    System.out.println("The shooter decided to play with " + actionGrenade.size() + "powerups");
                                                    PlayerAbstract playerShooter = null;
                                                    for (int b = 0; b < currentGame.getActivePlayers().size(); b++) {
                                                        if (currentGame.getActivePlayers().get(b).getClientID() == IDShooter)
                                                            playerShooter = currentGame.getActivePlayers().get(b);
                                                    }
                                                    System.out.println("The shooter is " + playerShooter.getName());
                                                    for (Info a : actionGrenade) {
                                                        System.out.println("test");
                                                        PowerUpAction powerUpAction = new PowerUpAction((PowerUpPack) a, currentGame, playerShooter);
                                                        turnHandler.setAndDoAction(powerUpAction);
                                                        System.out.println("Playing the tagback of color: " + ((PowerUpPack) a).getPowerupCard().getColor());
                                                    }
                                                    grenadeID = -1;
                                                    GrenadeAnswer grenadeDone = new GrenadeAnswer(-1);
                                                    gameManager.sendToEverybody(grenadeDone);
                                                    sendCollectShootAnswersRMI(currentPlayer, IDShooter);
                                                    ResetAnswer resetChoice = new ResetAnswer();
                                                    gameManager.sendToSpecific(resetChoice, IDShooter);
                                                }
                                            } catch (RemoteException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                    clientHasChosen = false;
                                    System.out.println("The player did the action ");
                                    listOfPlayers.get(i).setJustDamagedBy(null);
                                }
                                j = Constants.CHOICE;
                            }
                            j++;
                        }
                    }
                }

            }else{      //shoot action not valid
                sendErrorMessage(clientID);
            }
        }


        if(turnHandler.getCurrentPhase() == TurnPhase.END_TURN){
            System.out.println("We are in the end turn");
            //turnHandler.nextPhase();
            System.out.println("Turning next phase");
            System.out.println("Number of actions ended " +turnHandler.getCurrentPhase());
            //ChangeCurrentPlayerAnswer change = new ChangeCurrentPlayerAnswer();
            //server.sendToEverybody(change);
        }
        return true;
    }

    public void sendErrorMessage(int clientId) {
        gameManager.sendToSpecific(new MessageAnswer("\nAction not valid!\n"), clientId);
    }

    public void sendErrorMessage(int clientId, String message){
        gameManager.sendToSpecific(new MessageAnswer(message), clientId);

    }

    private void sendGrenadeAnswer() {
    gameManager.sendToEverybody(new GameBoardAnswer(this.currentGame.getCurrentGameBoard()));
    }
    /*public void setClientHasChosen(){
        this.clientHasChosen = true;
    }*/


    public void sendChangeCurrentPlayer(){
        ChangeCurrentPlayerAnswer changeAnswer = new ChangeCurrentPlayerAnswer();
        gameManager.sendToEverybody(changeAnswer);
    }

    public void sendCollectShootAnswersRMI(ConcretePlayer player, int clientID){
        GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(this.currentGame.getCurrentGameBoard());
        PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(player.getHand());
        gameManager.sendToEverybody(gameBoardAnswer);
        gameManager.sendToSpecific(playerHandAnswer, clientID);
    }

    public void sendSquaresRestored(){
        gameManager.sendToEverybody(new GameBoardAnswer(this.currentGame.getCurrentGameBoard()));
    }

    public Game getCurrentGame(){
        return this.currentGame;
    }

    public void setCurrentID(int i){
        this.currentID = i;
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
        boolean doubleKill = false;
        PlayerBoard board;
        for(PlayerAbstract player : this.currentGame.getActivePlayers()){
            if(player.getPlayerBoard().getDamageTaken() > Constants.DEATH_THRESHOLD){
                distributePoints(player);
                if(doubleKill){
                    currentGame.getPlayerFromColor(player.getKillerColor()).addPoints(1);
                }
                player.die();
                skullsToAdd = player.isOverkilled() ? 2 : 1;
                this.getCurrentGame().getCurrentGameBoard().getTrack().removeSkull(skullsToAdd,player.getKillerColor());
                sendSpawnAnswer(player);
                if(player.isOverkilled()){
                    board = this.getCurrentGame().getCurrentGameBoard().getBoardFromColor(player.getKillerColor());
                    board.addMarks(1,player.getColor());
                }
                needToSpawn = true;
                doubleKill = true;
            }
        }
        return needToSpawn;
    }

    private void distributePoints(PlayerAbstract player) {
        final int FIRST_DAMAGE = 0;
        //first blood gets 1 point more
        currentGame.getPlayerFromColor(player.getPlayerBoard().getDamage().get(FIRST_DAMAGE)).addPoints(1);

        //most damage dealt gets max points
        //tie breaker: if 2 or more dealt the same amount then the first to deal damage gets the most points
        List<PlayerAbstract> attackers = getAttackers(player);
        int i = 0;
        for(PlayerAbstract attacker : attackers){
            attacker.addPoints(player.getPlayerBoard().getPointValueArray()[player.getPlayerBoard().getCurrentPointValueCursor() + i]);
            i++;
        }
        //double kills give 1 point more
    }

    private List<PlayerAbstract> getAttackers(PlayerAbstract player) {
        List<PlayerAbstract> playersInOrder = new ArrayList<>();
        Map<Color,Integer> colorIntegerMap = new EnumMap<>(Color.class);
        int damage;
        for(PlayerAbstract playerAbstract : currentGame.getActivePlayers()){
            damage = player.getPlayerBoard().getDamageOfAColor(playerAbstract.getColor());
            if(damage > 0){
                colorIntegerMap.put(playerAbstract.getColor(),damage);
            }
        }
        List<PlayerAbstract> players = new ArrayList<>();
        while(!colorIntegerMap.isEmpty()) {
            int max = 0;
            for (Map.Entry<Color, Integer> entry : colorIntegerMap.entrySet()) {
                if (entry.getValue() > max) {
                    players.clear();
                    max = entry.getValue();
                    players.add(currentGame.getPlayerFromColor(entry.getKey()));
                }else if(entry.getValue() == max){
                    players.add(currentGame.getPlayerFromColor(entry.getKey()));
                }
            }
            for(PlayerAbstract playersToRemove : players){
                colorIntegerMap.remove(playersToRemove.getColor());
            }
            if(players.size() > 1){
                playersInOrder.addAll(tieBreaker(player,players));
            }else{
                playersInOrder.add(players.get(0));
            }
        }
        return playersInOrder;
    }

    private List<PlayerAbstract> tieBreaker(PlayerAbstract player, List<PlayerAbstract> attackers) {
        List<PlayerAbstract> result = new ArrayList<>();
        List<Color> attackersInOrder = player.getPlayerBoard().getAttackersInOrder();
        for(Color c : attackersInOrder){
            if(attackers.contains(currentGame.getPlayerFromColor(c)))result.add(currentGame.getPlayerFromColor(c));
        }
        return result;
    }

    public List<PlayerAbstract> getPlayersToSpawn(){
        return playersToSpawn;
    }

    private void sendSpawnAnswer(PlayerAbstract playerAbstract) {
        this.playersToSpawn.add(playerAbstract);
        gameManager.sendToSpecific(new SetSpawnAnswer(true),playerAbstract.getClientID());
        gameManager.sendToEverybody(new PlayerDiedAnswer());
        gameManager.sendToEverybody(new GameBoardAnswer(this.currentGame.getCurrentGameBoard()));
    }

    public void sendSpawnRequest() {
        //also draws a powerupcard
        for(PlayerAbstract player : this.playersToSpawn){
            player.drawPowerup();
            gameManager.sendToSpecific(new PlayerHandAnswer(player.getHand()), player.getClientID());
            gameManager.sendToSpecific(new SpawnCommandAnswer(), player.getClientID());
        }
    }
}
