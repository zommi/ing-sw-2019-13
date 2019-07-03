package server.controller;

import client.Info;
import client.ReloadInfo;
import client.SquareInfo;
import client.powerups.PowerUpPack;
import client.weapons.Weapon;
import constants.Color;
import constants.Constants;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import server.GameManager;
import server.Server;
import server.controller.turns.TurnPhase;
import server.model.cards.PowerUp;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.game.Game;
import server.model.game.GameState;
import server.model.gameboard.GameBoard;
import server.model.gameboard.PowerupDeck;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    public void pointDistributionTest(){
        Controller controller = new Controller(1,5,new GameManager(new Server()));
        PlayerAbstract mimmo = new ConcretePlayer("mimmo");
        PlayerAbstract pluto = new ConcretePlayer("pluto");
        PlayerAbstract pippo = new ConcretePlayer("pippo");
        PlayerAbstract nino = new ConcretePlayer("nino");

        mimmo.setCurrentGame(controller.getCurrentGame());
        pluto.setCurrentGame(controller.getCurrentGame());
        pippo.setCurrentGame(controller.getCurrentGame());
        nino.setCurrentGame(controller.getCurrentGame());
        controller.getCurrentGame().addPlayer(mimmo);
        controller.getCurrentGame().addPlayer(pluto);
        controller.getCurrentGame().addPlayer(pippo);
        controller.getCurrentGame().addPlayer(nino);
        mimmo.setPlayerCharacter(Figure.BANSHEE);
        pluto.setPlayerCharacter(Figure.DESTRUCTOR);
        pippo.setPlayerCharacter(Figure.SPROG);
        nino.setPlayerCharacter(Figure.VIOLET);

        pluto.addDamage(4, Color.GREEN); //pippo
        pluto.addDamage(3, Color.PURPLE); //nino
        pluto.addDamage(5, Color.BLUE); //mimmo

        controller.handleDeaths();

        assertEquals(7,pippo.getPoints());
        assertEquals(4,nino.getPoints());
        assertEquals(8,mimmo.getPoints());

        pluto.addDamage(4, Color.GREEN); //pippo
        pluto.addDamage(3, Color.PURPLE); //nino
        pluto.addDamage(4, Color.BLUE); //mimmo

        controller.handleDeaths();

        assertEquals(14,pippo.getPoints()); //7+6+1
        assertEquals(6,nino.getPoints());   //4+2
        assertEquals(12,mimmo.getPoints()); //8+4


    }

    @Test
    public void makeActionTest(){
        Controller controller = new Controller(1, 5, new GameManager(new Server()));
        Game game = new Game(1,5, controller);
        PlayerAbstract player1 = new ConcretePlayer("Alessia");
        player1.getHand().addCard(game.getCurrentGameBoard().getPowerUpCard(8));
        player1.setCurrentGame(controller.getCurrentGame());
        controller.getCurrentGame().addPlayer(player1);
        controller.getCurrentGame().getTurnHandler().setCurrentTurnPhase(TurnPhase.END_TURN);
        controller.getCurrentGame().getTurnHandler().startNextPlayerTimer();
        GameBoard gameBoard = new GameBoard(1, 5);
        player1.getHand().addCard(gameBoard.getWeaponCard("Tractor Beam")); //now he has the tractor beam in his hand.
        player1.getHand().getWeaponHand().get(0).setReady(false);
        List<PowerUpCard> powerups = new ArrayList<>();
        powerups.add(controller.getCurrentGame().getCurrentGameBoard().getPowerupDeck().draw());
        player1.getHand().addCard(powerups.get(0));
        Info action = new ReloadInfo(player1.getHand().getWeaponHand(), powerups);
        controller.makeAction(0, action);
        assertEquals(true, player1.getHand().getWeaponHand().get(0).isReady());


        PlayerAbstract player2 = new ConcretePlayer("Tommaso");
        player2.setPlayerCharacter(Figure.SPROG);
        game.getActivePlayers().add(player2);
        player2.spawn(game.getCurrentGameBoard().getMap().getSquare(0,0));
        controller.getCurrentGame().addPlayer(player2);
        SquareInfo squareInfo = new SquareInfo(0, 1);
        controller.getCurrentGame().getTurnHandler().setCurrentTurnPhase(TurnPhase.FIRST_ACTION);
        Info actionPowerup = new PowerUpPack(gameBoard.getPowerUpCard(8), squareInfo, "Tommaso");
        System.out.println("" +gameBoard.getPowerUpCard(8).getName());
        controller.makeAction(0, actionPowerup);
        assertEquals(squareInfo.getCol(), player2.getPosition().getCol());
        assertEquals(squareInfo.getRow(), player2.getPosition().getRow());

    }


    /*
    @Test
    public void doublekillTest() throws RemoteException{
        Controller controller = new Controller(1,5,new GameManager(new Server()));
        PlayerAbstract mimmo = new ConcretePlayer("mimmo");
        PlayerAbstract pluto = new ConcretePlayer("pluto");
        PlayerAbstract pippo = new ConcretePlayer("pippo");

        controller.getCurrentGame().addPlayer(mimmo);
        controller.getCurrentGame().addPlayer(pluto);
        controller.getCurrentGame().addPlayer(pippo);
        mimmo.setPlayerCharacter(Figure.BANSHEE);
        pluto.setPlayerCharacter(Figure.DESTRUCTOR);
        pippo.setPlayerCharacter(Figure.SPROG);

        pluto.addDamage(11, Color.BLUE); //mimmo
        pippo.addDamage(11, Color.BLUE); //mimmo

        controller.handleDeaths();

        assertEquals(19,mimmo.getPoints()); //8+8+1+1+1
    }
    */
}