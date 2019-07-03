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

    private Game currentGame;

    private GameMap currentMap;

    private GameManager gameManager;

    private List<SquareAbstract> squaresToUpdate;

    private List<PlayerAbstract> playersToRespawn;

    private boolean finalFrenzy;

    private PlayerAbstract firstFrenzyPlayer;

    public Controller(int mapChoice, int initialSkulls, GameManager gameManager){
        this.currentGame = new Game(mapChoice, initialSkulls, this);
        this.currentMap = this.currentGame.getCurrentGameMap();
        this.gameManager = gameManager;
        this.squaresToUpdate = new ArrayList<>();
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
                System.out.println("Exiting from RESPAWN_PHASE");
                turnHandler.nextPhase();
            } else{
                System.out.println("Not exiting from RESPAWN_PHASE because there are other players that have to spawn");
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

    public PlayerAbstract getFirstFrenzyPlayer() {
        return firstFrenzyPlayer;
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
        int tokensToAdd;
        boolean needToSpawn = false;
        boolean doubleKill = false;
        PlayerBoard board;
        playersToRespawn.clear();

        for(PlayerAbstract player : this.currentGame.getActivePlayers()){
            if(player.getPlayerBoard().getDamageTaken() > Constants.DEATH_THRESHOLD){

                PlayerAbstract killer = currentGame.getPlayerFromColor(player.getKillerColor());

                //informing players of this death
                String message = player.getName() + (player.isOverkilled() ? " got overkilled by " : " died from ") +
                        killer.getName();
                gameManager.sendToEverybody(new MessageAnswer(message));

                distributePoints(player);
                if(doubleKill){
                    killer.addPoints(Constants.DOUBLE_KILL_POINTS);
                    gameManager.sendToEverybody(new MessageAnswer(killer.getName() + " got " + Constants.DOUBLE_KILL_POINTS + " points!"));

                }

                player.die();
                tokensToAdd = player.isOverkilled() ? 2 : 1;

                this.getCurrentGame().getCurrentGameBoard().getTrack().swapDamageSkull(tokensToAdd,player.getKillerColor());
                finalFrenzy = currentGame.getCurrentGameBoard().getTrack().getRemainingSkulls() <= 0;

                playersToRespawn.add(player);

                player.drawPowerupNoLimits();

                gameManager.sendToSpecific(new PlayerHandAnswer(player.getHand()), player.getClientID());
                gameManager.sendToSpecific(new SetRespawnAnswer(),player.getClientID());
                gameManager.sendToEverybody(new GameBoardAnswer(currentGame.getCurrentGameBoard()));

                if(player.isOverkilled()){
                    board = killer.getPlayerBoard();
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

        firstFrenzyPlayer = currentGame.getCurrentPlayer();     //assuming it has already changed

        if(firstFrenzyPlayer.equals(currentGame.getActivePlayers().get(0))) {
            for (PlayerAbstract playerAbstract : currentGame.getActivePlayers()) {
                playerAbstract.setState(PlayerState.AFTER_FIRST_PLAYER_FF);
            }
        }
        else{
            for(PlayerAbstract playerAbstract : currentGame.getActivePlayers()){
                if(currentGame.getActivePlayers().indexOf(playerAbstract) >= currentGame.getActivePlayers().indexOf(firstFrenzyPlayer))
                    playerAbstract.setState(PlayerState.BEFORE_FIRST_PLAYER_FF);
                else
                    playerAbstract.setState(PlayerState.AFTER_FIRST_PLAYER_FF);
            }
        }
    }

    private void distributePoints(PlayerAbstract player) {
        //first blood gets 1 point more
        if(!player.getPlayerBoard().isTurned() && !player.getPlayerBoard().getDamage().isEmpty()) {
            PlayerAbstract playerToAddPoints = currentGame.getPlayerFromColor(player.getPlayerBoard().getFirstDamageColor());
            playerToAddPoints.addPoints(Constants.FIRST_BLOOD_POINTS);
            gameManager.sendToEverybody(new MessageAnswer(playerToAddPoints.getName() + " got " + Constants.FIRST_BLOOD_POINTS + " points for First Blood!"));
        }

        //most damage dealt gets max points
        //tie breaker: if 2 or more dealt the same amount then the first to deal damage gets the most points
        List<PlayerAbstract> attackers = getAttackers(player);
        int i = 0;
        int points;
        for(PlayerAbstract attacker : attackers){
            points = i < Constants.POINTS_VALUES.length ?
                    player.getPlayerBoard().getPointValueArray()[player.getPlayerBoard().getCurrentPointValueCursor() + i] :
                    Constants.DEFAULT_MIN_POINTS;
            attacker.addPoints(points);
            gameManager.sendToEverybody(new MessageAnswer(attacker.getName() + " got " + points + " points for killing " + player.getName()));
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
        computeTrack();
    }

    private void computeTrack() {
        int[] pointsValues = Constants.KILLSHOTTRACK_POINTS_VALUES;
        Map<Color, Integer> colorIntegerMap = new HashMap<>();
        for (Color c : Color.values()) {
            if(c.isCharacterColor()) {
                colorIntegerMap.put(c, currentGame.getCurrentGameBoard().getTrack().getTokensOfColor(c));
                System.out.println(colorIntegerMap.get(c) + " " + c.name() + " tokens");
            }

        }
        List<PlayerAbstract> playersInOrder = new ArrayList<>();
        int max = 0;

        Color color = Color.UNDEFINED;
        while (!colorIntegerMap.isEmpty()){
            max = 0;
            Iterator<Map.Entry<Color, Integer>> iterator = colorIntegerMap.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<Color, Integer> entry = iterator.next();
                if(entry.getValue() == 0) {
                    iterator.remove();
                    continue;
                }
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    color = entry.getKey();
                    System.out.println("Max found: " + max + " color " + color.name());
                }
            }
            playersInOrder.add(currentGame.getPlayerFromColor(color));
            colorIntegerMap.remove(color);

        }

        if(max == 0)
            return;

        for(int i = 0; i < playersInOrder.size(); i++){
            int points = i < pointsValues.length ? pointsValues[i] : Constants.DEFAULT_MIN_POINTS;
            playersInOrder.get(i).addPoints(points);
            gameManager.sendToEverybody(new MessageAnswer(playersInOrder.get(i).getName() + " got " + points + " points from the killshot track!"));
        }
    }
}
