package server.controller;

import client.*;
import client.powerups.PowerUpPack;
import client.weapons.ShootPack;
import constants.Color;
import constants.Constants;
import server.GameManager;
import server.SetRespawnAnswer;
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

    private boolean clientHasChosen;

    private GameManager gameManager;

    private List<SquareAbstract> squaresToUpdate;

    private List<PlayerAbstract> playersToRespawn;

    public Controller(int mapChoice, int initialSkulls, GameManager gameManager){
        this.currentGame = new Game(mapChoice, initialSkulls, this);
        this.currentMap = this.currentGame.getCurrentGameMap();
        this.gameManager = gameManager;
        this.currentID = 0;
        this.grenadeID = -1;
        this.squaresToUpdate = new ArrayList<>();
        this.clientHasChosen = false;
        this.playersToRespawn = new ArrayList<>();
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

    public String getCurrentCharacterName(){
        for(PlayerAbstract playerAbstract : currentGame.getActivePlayers()){
            if(playerAbstract.getClientID() == currentID)
                //return playerAbstract.getName() + ", " + playerAbstract.getCharacterName();
                return playerAbstract.getCharacterName();
        }
        return "No one is playing";
    }

    public void makeAsynchronousAction(int clientID, Info action){
        //this is meant to do actions out of your turn

        PlayerAbstract player = currentGame.getPlayerFromId(clientID);
        TurnHandler turnHandler = currentGame.getTurnHandler();

        if(action instanceof SpawnInfo){
            SpawnAction spawnAction = new SpawnAction((SpawnInfo) action, player, currentGame.getCurrentGameBoard());
            if(!turnHandler.setAndDoSpawn(spawnAction)){
                return;
            }
            gameManager.sendToEverybody(new GameBoardAnswer(currentGame.getCurrentGameBoard()));
            //spawn is disabled locally by the client
            //gameManager.sendToSpecific(new SetSpawnAnswer(false), clientID);
            gameManager.sendToSpecific(new PlayerHandAnswer(player.getHand()), clientID);

            this.playersToRespawn.remove(currentGame.getPlayerFromId(clientID));
            if(this.playersToRespawn.isEmpty()){
                System.out.println("Exiting from SPAWN_PHASE");
                turnHandler.nextPhase();
            }
        }

        else if(action instanceof PowerUpPack){
            PowerUpAction powerUpAction = new PowerUpAction((PowerUpPack) action, currentGame, player);
            if(turnHandler.setAndDoTagback(powerUpAction))
                sendGameboardAndHand(player);
            else
                sendErrorMessage(clientID);
        }

        else if (action instanceof TagbackStopInfo){
            turnHandler.tagbackStop(clientID);
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

        if(currentGame.getTurnHandler().getCurrentPhase() == TurnPhase.TAGBACK_PHASE){
            sendErrorMessage(clientID, "Wait for the other players to play their tagback grenades");
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
                sendGameboardAndHand(currentPlayer);
            else
                sendErrorMessage(clientID);
        }

        else if(action instanceof SpawnInfo){
            SpawnAction spawnAction = new SpawnAction((SpawnInfo) action, currentPlayer, currentGame.getCurrentGameBoard());
            if(turnHandler.setAndDoAction(spawnAction)) {
                gameManager.sendToEverybody(new GameBoardAnswer(currentGame.getCurrentGameBoard()));
                //spawn is disabled locally by the client
                //gameManager.sendToSpecific(new SetSpawnAnswer(false), currentID);
                gameManager.sendToSpecific(new PlayerHandAnswer(currentPlayer.getHand()), currentID);
            }else{
                sendErrorMessage(clientID);
            }
        }

        else if(action instanceof MoveInfo){
            MoveAction moveAction = new MoveAction((MoveInfo) action, currentPlayer, currentMap);
            if(turnHandler.setAndDoAction(moveAction)) {
                GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(this.currentGame.getCurrentGameBoard());
                String message = currentPlayer.getName() + " moved";
                gameManager.sendEverybodyExcept(new MessageAnswer(message), currentPlayer.getClientID());
                gameManager.sendToEverybody(gameBoardAnswer);
            }else{
                sendErrorMessage(clientID);
            }
        }

        else if(action instanceof CollectInfo){
            MoveInfo temp = new MoveInfo(((CollectInfo)action).getRow(),((CollectInfo)action).getCol());
            CollectAction collectAction = new CollectAction(temp, (CollectInfo) action, currentPlayer, currentMap);
            if(turnHandler.setAndDoAction(collectAction))
                this.sendGameboardAndHand(currentPlayer);
            else
                sendErrorMessage(clientID);
        }

        else if(action instanceof ShootPack) {
            //resetting setDamagedBy
            //if we are here it is because the current player can do the shoot action now, even if it won't be validated
            for(PlayerAbstract playerAbstract : currentGame.getActivePlayers()){
                playerAbstract.setJustDamagedBy(null);
            }

            ShootAction shootAction = new ShootAction((ShootPack) action, currentPlayer, currentGame);
            if(turnHandler.setAndDoAction(shootAction)) {
                sendGameboardAndHand(currentPlayer);


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

    public void sendChangeCurrentPlayer(){
        ChangeCurrentPlayerAnswer changeAnswer = new ChangeCurrentPlayerAnswer();
        gameManager.sendToEverybody(changeAnswer);
    }

    public void sendGameboardAndHand(PlayerAbstract player){
        GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(this.currentGame.getCurrentGameBoard());
        PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(player.getHand());
        gameManager.sendToEverybody(gameBoardAnswer);
        gameManager.sendToSpecific(playerHandAnswer, player.getClientID());
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
        playersToRespawn.clear();

        for(PlayerAbstract player : this.currentGame.getActivePlayers()){
            if(player.getPlayerBoard().getDamageTaken() > Constants.DEATH_THRESHOLD){
                distributePoints(player);
                if(doubleKill){
                    currentGame.getPlayerFromColor(player.getKillerColor()).addPoints(1);
                }
                player.die();
                skullsToAdd = player.isOverkilled() ? 2 : 1;

                //informing players of this death
                String message = player.getName() + (player.isOverkilled() ? " died from " : " got overkilled by ") +
                        currentGame.getPlayerFromColor(player.getKillerColor()).getName();
                gameManager.sendToEverybody(new MessageAnswer(message));

                this.getCurrentGame().getCurrentGameBoard().getTrack().removeSkull(skullsToAdd,player.getKillerColor());

                playersToRespawn.add(player);

                player.drawPowerup();

                gameManager.sendToSpecific(new PlayerHandAnswer(player.getHand()), player.getClientID());
                gameManager.sendToSpecific(new SetRespawnAnswer(),player.getClientID());
                gameManager.sendToEverybody(new GameBoardAnswer(currentGame.getCurrentGameBoard()));

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

    public List<PlayerAbstract> getPlayersToRespawn(){
        return playersToRespawn;
    }
}
