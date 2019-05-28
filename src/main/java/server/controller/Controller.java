package server.controller;

import client.CollectInfo;
import client.Info;
import client.MoveInfo;
import client.weapons.ShootPack;
import exceptions.WrongGameStateException;
import server.Server;
import server.controller.playeraction.*;
import server.controller.playeraction.normalaction.CollectAction;
import server.controller.playeraction.normalaction.MoveAction;
import server.controller.playeraction.normalaction.ShootAction;
import server.controller.turns.TurnHandler;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.map.GameMap;
import server.model.player.ConcretePlayer;
import server.model.player.PlayerAbstract;
import server.model.player.PlayerState;
import view.GameBoardAnswer;
import view.MapAnswer;
import view.PlayerBoardAnswer;
import view.PlayerHandAnswer;

import java.util.List;

public class Controller implements MyObserver {

    //private List<PlayerAbstract> players = new ArrayList<>();

    private int currentID;

    private Game currentGame;

    private GameMap currentMap;

    private Server server;


    public Controller(int mapChoice, int initialSkulls, Server server){
        this.currentGame = new Game(mapChoice, initialSkulls);
        this.currentMap = this.currentGame.getCurrentGameMap();
        this.server = server;
    }

    public int getCurrentID(){
        return this.currentID;
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
        //TODO initialize currentID and handle the turns.
        ConcretePlayer currentPlayer = (ConcretePlayer) currentGame.getCurrentPlayer();


        if (currentPlayer.getPlayerState().equals(PlayerState.DISCONNECTED) || currentGame.getCurrentState().equals(GameState.END_GAME)) {
            return false;
        }


        if(action instanceof MoveInfo){
            MoveAction moveAction = new MoveAction((MoveInfo) action);
            turnHandler.setAndDoAction(moveAction);
            MapAnswer mapAnswer = new MapAnswer(this.currentGame.getCurrentGameMap());
            GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(this.currentGame.getCurrentGameBoard());
            server.sendToEverybodyRMI(mapAnswer);
            server.sendToEverybodyRMI(gameBoardAnswer);
        }
        else if(action instanceof CollectInfo){
            MoveInfo temp = new MoveInfo(((CollectInfo)action).getCoordinateX(),((CollectInfo)action).getCoordinateY());
            CollectAction collectAction = new CollectAction(temp, (CollectInfo) action);
            turnHandler.setAndDoAction(collectAction);
            this.sendCollectShootAnswers(currentPlayer);
        }
        else if(action instanceof ShootPack){
            ShootAction shootAction = new ShootAction((ShootPack) action);
            turnHandler.setAndDoAction(shootAction);
            sendCollectShootAnswers(currentPlayer);
        }
        return true;
    }


    public void sendCollectShootAnswers(ConcretePlayer player){
        MapAnswer mapAnswer = new MapAnswer(this.currentGame.getCurrentGameMap());
        GameBoardAnswer gameBoardAnswer = new GameBoardAnswer(this.currentGame.getCurrentGameBoard());
        PlayerBoardAnswer playerBoardAnswer = new PlayerBoardAnswer(player.getBoard());
        PlayerHandAnswer playerHandAnswer = new PlayerHandAnswer(player.getHand());
        server.sendToEverybodyRMI(mapAnswer);
        server.sendToEverybodyRMI(gameBoardAnswer);
        server.sendToEverybodyRMI(playerBoardAnswer);
        server.sendToEverybodyRMI(playerHandAnswer);
    }

    public Game getCurrentGame(){
        return this.currentGame;
    }

    public void nextPlayer() throws WrongGameStateException {
        this.currentGame.nextPlayer();
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
