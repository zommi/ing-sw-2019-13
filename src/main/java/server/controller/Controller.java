package server.controller;

import client.*;
import client.weapons.ShootPack;
import exceptions.WrongGameStateException;
import server.Server;
import server.controller.playeraction.*;
import server.controller.playeraction.normalaction.CollectAction;
import server.controller.playeraction.normalaction.MoveAction;
import server.controller.playeraction.normalaction.ShootAction;
import server.controller.turns.TurnHandler;
import server.controller.turns.TurnPhase;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.map.GameMap;
import server.model.player.ConcretePlayer;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;
import view.*;

import java.util.List;

public class Controller implements MyObserver {

    private int currentID;

    private Game currentGame;

    private GameMap currentMap;

    private Server server;


    public Controller(int mapChoice, int initialSkulls, Server server){
        this.currentGame = new Game(mapChoice, initialSkulls);
        this.currentMap = this.currentGame.getCurrentGameMap();
        this.server = server;
        this.currentID = 0;
    }

    public int getCurrentID(){
        return this.currentID;
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
        for(int i = 0; i < currentGame.getActivePlayers().size(); i++){
            if(currentGame.getActivePlayers().get(i).getClientID() == currentID)
                return currentGame.getActivePlayers().get(i).getCharacterName();
        }
        return "No one is playing";
    }

    public boolean makeAction(int clientID, Info action){
        TurnHandler turnHandler = currentGame.getTurnHandler();  //the phase depends on the action the player is sending!! it may be the first, the second or the third one
        turnHandler.setController(this);
        ConcretePlayer currentPlayer = (ConcretePlayer) currentGame.getCurrentPlayer();
        turnHandler.setCurrentPlayer(currentPlayer);

        System.out.println("We are in the game state: " +currentGame.getCurrentState());
        if(turnHandler.getCurrentPhase() == TurnPhase.END_TURN){
            this.nextCurrentID();
        }

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

        if ((currentPlayer.getPlayerState().equals(PlayerState.DISCONNECTED)) || (currentGame.getCurrentState().equals(GameState.END_GAME))) {
            return false;
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
        }

        if(action instanceof MoveInfo){
            MoveAction moveAction = new MoveAction((MoveInfo) action, currentPlayer, currentMap);
            turnHandler.setAndDoAction(moveAction);
            MapAnswer mapAnswer = new MapAnswer(this.currentGame.getCurrentGameMap());
            GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(this.currentGame.getCurrentGameBoard());
            server.sendToEverybodyRMI(mapAnswer);
            server.sendToEverybodyRMI(gameBoardAnswer);
        }
        else if(action instanceof CollectInfo){
            MoveInfo temp = new MoveInfo(((CollectInfo)action).getCoordinateX(),((CollectInfo)action).getCoordinateY());
            CollectAction collectAction = new CollectAction(temp, (CollectInfo) action, currentPlayer, currentMap);
            turnHandler.setAndDoAction(collectAction);
            this.sendCollectShootAnswersRMI(currentPlayer, clientID);
        }
        else if(action instanceof ShootPack){
            ShootAction shootAction = new ShootAction((ShootPack) action); // TODO add player
            turnHandler.setAndDoAction(shootAction);
            sendCollectShootAnswersRMI(currentPlayer, clientID);
        }
        return true;
    }


    public void sendChangeCurrentPlayer(){
        ChangeCurrentPlayerAnswer changeAnswer = new ChangeCurrentPlayerAnswer();
        server.sendToEverybodyRMI(changeAnswer);
    }

    public void sendCollectShootAnswersRMI(ConcretePlayer player, int clientID){
        MapAnswer mapAnswer = new MapAnswer(this.currentGame.getCurrentGameMap());
        GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(this.currentGame.getCurrentGameBoard());
        PlayerBoardAnswer playerBoardAnswer = new PlayerBoardAnswer(player.getBoard());
        PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(player.getHand());
        server.sendToEverybodyRMI(mapAnswer);
        server.sendToEverybodyRMI(gameBoardAnswer);
        server.sendToEverybodyRMI(playerBoardAnswer);
        server.sendToSpecificRMI(playerHandAnswer, clientID);
    }

    public Game getCurrentGame(){
        return this.currentGame;
    }


    public void addClientInMap(PlayerAbstract player){
        try{
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

    public void update(){}

    public void startGame(){

    }

}
