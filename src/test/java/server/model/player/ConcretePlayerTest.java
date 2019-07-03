package server.model.player;

import constants.Color;
import constants.Constants;
import constants.Direction;
import server.GameManager;
import server.Server;
import server.controller.Controller;
import server.model.gameboard.GameBoard;
import server.model.map.*;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ConcretePlayerTest {
    //TODO when map and cards are somewhat done

    @Test
    public void testCreator(){
        GameBoard testGb = new GameBoard(1,8);
        PlayerAbstract player1 = new ConcretePlayer("Pippo");
        player1.setPlayerCharacter(Figure.DESTRUCTOR);
        PlayerAbstract player2 = new ConcretePlayer("Pluto");
        player2.setPlayerCharacter(Figure.BANSHEE);


        List<GameCharacter> list1 = new ArrayList<GameCharacter>();
        list1.add(player1.getGameCharacter());
        List<GameCharacter> list2 = new ArrayList<GameCharacter>();
        list2.add(player2.getGameCharacter());

        player1.spawn(testGb.getMap().getSpawnPoint(Color.BLUE));
        player2.spawn(testGb.getMap().getSpawnPoint(Color.YELLOW));
        assertEquals(testGb.getMap().getSpawnPoint(Color.BLUE).getCharacters(), list1);
        assertEquals(testGb.getMap().getSpawnPoint(Color.YELLOW).getCharacters(), list2);

        testGb.getMap().getSpawnPoint(Color.BLUE).removeCharacter(player1.getGameCharacter());
        testGb.getMap().getSpawnPoint(Color.YELLOW).removeCharacter(player2.getGameCharacter());

    }

    @Test
    public void collectTest(){
        Controller controller = new Controller(1,8,new GameManager(new Server()));
        PlayerAbstract player1 = new ConcretePlayer("Pippo");
        player1.setCurrentGame(controller.getCurrentGame());
        controller.getCurrentGame().addPlayer(player1);
        player1.setPlayerCharacter(Figure.DESTRUCTOR);
        player1.spawn(controller.getCurrentGame().getCurrentGameMap().getSpawnPoint(Color.BLUE));
        player1.collect(controller.getCurrentGame().getCurrentGameMap().getSpawnPoint(Color.BLUE),1);
        player1.setPosition(controller.getCurrentGame().getCurrentGameMap().getSquare(0,0));
        player1.collect((Square) player1.getPosition());
    }

    @Test
    public void drawPowerupTest(){
        Controller controller = new Controller(1,8,new GameManager(new Server()));
        PlayerAbstract player1 = new ConcretePlayer("Pippo");
        player1.setCurrentGame(controller.getCurrentGame());
        controller.getCurrentGame().addPlayer(player1);
        player1.setPlayerCharacter(Figure.DESTRUCTOR);
        for (int i = 0 ; i < Constants.MAX_POWERUP_HAND; i++)player1.drawPowerup();
        assertEquals(3, player1.getHand().getPowerupHand().size());
        player1.drawPowerupNoLimits();
        assertEquals(4, player1.getHand().getPowerupHand().size());
    }


}