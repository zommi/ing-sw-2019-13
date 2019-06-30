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

    private boolean finalFrenzy;

    public Controller(int mapChoice, int initialSkulls, GameManager gameManager){
        this.currentGame = new Game(mapChoice, initialSkulls, this);
        this.currentMap = this.currentGame.getCurrentGameMap();
        this.gameManager = gameManager;
        this.currentID = 0;
        this.grenadeID = -1;
        this.squaresToUpdate = new ArrayList<>();
        this.clientHasChosen = false;
        this.playersToRespawn = new ArrayList<>();
        this.finalFrenzy = false;
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

        System.out.println("Processing " + action.getClass().toString().toUpperCase() +
                "\nWe are in the game state: " +currentGame.getCurrentState());
        System.out.println("We are in the action number: " +currentGame.getTurnHandler().getCurrentTurnPhase());

        if(currentGame.getCurrentState().equals(GameState.GAME_OVER)){
            sendErrorMessage(clientID, "You cannot do that now, game is over!");
            return;
        }

        PlayerAbstract player = currentGame.getPlayerFromId(clientID);
        TurnHandler turnHandler = currentGame.getTurnHandler();

        if(action instanceof SpawnInfo){
            //spawn doesn't change the turn phase so we have to send gameboard here
            SpawnAction spawnAction = new SpawnAction((SpawnInfo) action, player, currentGame.getCurrentGameBoard());
            if(!turnHandler.setAndDoSpawn(spawnAction)){
                return;
            }
            gameManager.sendToEverybody(new GameBoardAnswer(currentGame.getCurrentGameBoard()));
            //spawn is disabled locally by the client
            gameManager.sendToSpecific(new PlayerHandAnswer(player.getHand()), clientID);

            this.playersToRespawn.remove(currentGame.getPlayerFromId(clientID));
            if(this.playersToRespawn.isEmpty()){
                System.out.println("Exiting from SPAWN_PHASE");
                turnHandler.nextPhase();
            } else{
                System.out.println("Not exiting from SPAWN_PHASE because there are other players that have to spawn");
            }
        }

        else if(action instanceof PowerUpPack){
            //powerup doesn't change the turn phase so we have to send gameboard here

            PowerUpAction powerUpAction = new PowerUpAction((PowerUpPack) action, currentGame, player);
            if(turnHandler.setAndDoTagback(powerUpAction))
                sendEverybodyGameboardAndHand();
            else
                sendErrorMessage(clientID);
        }

        else if (action instanceof TagbackStopInfo){
            turnHandler.tagbackStop(clientID);
        }
    }

    public void makeAction(int clientID, Info action){
        TurnHandler turnHandler = currentGame.getTurnHandler();  //the phase depends on the action the player is sending!! it may be the first, the second or the third one
        ConcretePlayer currentPlayer = (ConcretePlayer) currentGame.getCurrentPlayer();

        System.out.println("Processing " + action.getClass().toString().toUpperCase() +
                "\nWe are in the game state: " +currentGame.getCurrentState());
        System.out.println("We are in the action number: " +turnHandler.getCurrentTurnPhase());

        if (!currentPlayer.isConnected() ||
                currentID != clientID ||
                currentPlayer.getPlayerState().equals(PlayerState.DEAD)){
            sendErrorMessage(clientID, "You cannot do that now");
            return;
        }

        if(currentGame.getCurrentState().equals(GameState.GAME_OVER)){
            sendErrorMessage(clientID, "You cannot do that now, game is over!");
            return;
        }

        if(currentGame.getTurnHandler().getCurrentTurnPhase() == TurnPhase.TAGBACK_PHASE){
            sendErrorMessage(clientID, "Please wait for the other players to play their tagback grenades");
            return;
        }

        if(turnHandler.getCurrentTurnPhase().equals(TurnPhase.POWERUP_TURN) && !(action instanceof PowerUpPack) &&
                !(action instanceof ReloadInfo)){
            sendErrorMessage(clientID);
            return;
        }


        //checks and executes actions

        boolean actionOk = true;

        if(action instanceof ReloadInfo){

            //checks that it's not the wrong time to reload
            if(turnHandler.getCurrentTurnPhase() != TurnPhase.END_TURN) {
                actionOk = false;
            }else {
                ReloadAction reloadAction = new ReloadAction((ReloadInfo) action, currentPlayer);
                actionOk = turnHandler.setAndDoAction(reloadAction);
            }
        }

        else if(action instanceof PowerUpPack){
            //powerup doesn't change the turn phase so we have to send gameboard here

            PowerUpAction powerUpAction = new PowerUpAction((PowerUpPack) action,currentGame, currentPlayer);
            actionOk = turnHandler.setAndDoAction(powerUpAction);
            if(actionOk)
                sendEverybodyGameboardAndHand();
        }

        else if(action instanceof SpawnInfo){
            //spawn doesn't change the turn phase so we have to send gameboard here

            SpawnAction spawnAction = new SpawnAction((SpawnInfo) action, currentPlayer, currentGame.getCurrentGameBoard());
            actionOk = turnHandler.setAndDoAction(spawnAction);
            if(actionOk)
                sendEverybodyGameboardAndHand();
            //spawn is disabled locally by the client
        }

        else if(action instanceof MoveInfo){
            MoveAction moveAction = new MoveAction((MoveInfo) action, currentPlayer, currentMap);
            actionOk = turnHandler.setAndDoAction(moveAction);
            if(actionOk) {
                String message = currentPlayer.getName() + " moved";
                gameManager.sendEverybodyExcept(new MessageAnswer(message), currentPlayer.getClientID());
            }
        }

        else if(action instanceof CollectInfo){
            MoveInfo moveInfo = new MoveInfo(((CollectInfo)action).getRow(),((CollectInfo)action).getCol());
            CollectAction collectAction = new CollectAction(moveInfo, (CollectInfo) action, currentPlayer, currentMap);
            actionOk = turnHandler.setAndDoAction(collectAction);
        }

        else if(action instanceof ShootPack) {
            //resetting setDamagedBy
            //if we are here it is because the current player can do the shoot action now, even if it won't be validated
            for(PlayerAbstract playerAbstract : currentGame.getActivePlayers()){
                playerAbstract.setJustDamagedBy(null);
            }

            ShootAction shootAction = new ShootAction((ShootPack) action, currentPlayer, currentGame);
            actionOk = turnHandler.setAndDoAction(shootAction);
        }

        if(!actionOk){
            sendErrorMessage(clientID);
        }
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

    public void sendEverybodyGameboardAndHand() {
        gameManager.sendToEverybody(new GameBoardAnswer(currentGame.getCurrentGameBoard()));
        for(PlayerAbstract playerAbstract : currentGame.getActivePlayers()){
            gameManager.sendToSpecific(new PlayerHandAnswer(playerAbstract.getHand()), playerAbstract.getClientID());
        }
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
                String message = player.getName() + (player.isOverkilled() ? " got overkilled by " : " died from ") +
                        currentGame.getPlayerFromColor(player.getKillerColor()).getName();
                gameManager.sendToEverybody(new MessageAnswer(message));

                this.getCurrentGame().getCurrentGameBoard().getTrack().removeSkull(skullsToAdd,player.getKillerColor());
                finalFrenzy = this.getCurrentGame().getCurrentGameBoard().getTrack().getRemainingSkulls() <= 0;

                playersToRespawn.add(player);

                player.drawPowerupNoLimits();

                gameManager.sendToSpecific(new PlayerHandAnswer(player.getHand()), player.getClientID());
                gameManager.sendToSpecific(new SetRespawnAnswer(),player.getClientID());
                gameManager.sendToEverybody(new GameBoardAnswer(currentGame.getCurrentGameBoard()));

                if(player.isOverkilled()){
                    board = getCurrentGame().getPlayerFromColor(player.getColor()).getPlayerBoard();
                    board.addMarks(1,player.getColor());
                }
                needToSpawn = true;
                doubleKill = true;
            }
        }
        return needToSpawn;
    }

    public void startFrenzy() {
        this.currentGame.nextState();
        //dal giocatore dopo quello corrente inizio a settare gli stati della frenzy
        for(PlayerAbstract playerAbstract : this.currentGame.getActivePlayers()){
            if(playerAbstract.getPlayerBoard().getDamageTaken() == 0)playerAbstract.getPlayerBoard().turnPlayerBoard();
            playerAbstract.setState(currentGame.isAfterFirstPlayer(playerAbstract) ? PlayerState.AFTER_FIRST_PLAYER_FF : PlayerState.BEFORE_FIRST_PLAYER_FF);
        }
    }

    private void distributePoints(PlayerAbstract player) {
        final int FIRST_DAMAGE = 0;
        //first blood gets 1 point more
        if(!player.getPlayerBoard().isTurned()) {
            currentGame.getPlayerFromColor(player.getPlayerBoard().getDamage().get(FIRST_DAMAGE)).addPoints(1);
        }

        //most damage dealt gets max points
        //tie breaker: if 2 or more dealt the same amount then the first to deal damage gets the most points
        List<PlayerAbstract> attackers = getAttackers(player);
        int i = 0;
        int points;
        for(PlayerAbstract attacker : attackers){
            points = i < Constants.POINT_VALUE.length ?
                    player.getPlayerBoard().getPointValueArray()[player.getPlayerBoard().getCurrentPointValueCursor() + i] :
                    1;
            attacker.addPoints(points);
            i++;
        }
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

    public boolean isFinalFrenzy() {
        return finalFrenzy;
    }

    public void distributeEndGamePoints() {
        for(PlayerAbstract playerAbstract : currentGame.getActivePlayers()){
            distributePoints(playerAbstract);
        }
        currentGame.getCurrentGameBoard().getTrack().computeTrack(currentGame.getActivePlayers());
    }
}
