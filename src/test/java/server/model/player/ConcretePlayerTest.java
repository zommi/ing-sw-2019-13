package server.model.player;

import constants.Color;
import constants.Directions;
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
        PlayerAbstract player1 = new ConcretePlayer("Pippo", testGb, Figure.DESTRUCTOR);
        PlayerAbstract player2 = new ConcretePlayer("Pluto", testGb, Figure.BANSHEE);


        assertNotEquals(player1.getId(),player2.getId());

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
    public void moveTest(){
        GameBoard testGb = new GameBoard(1,8);
        PlayerAbstract player1 = new ConcretePlayer("Pippo", testGb, Figure.DESTRUCTOR);

        player1.spawn(testGb.getMap().getSpawnPoint(Color.BLUE));
        SquareAbstract currentSquare = testGb.getMap().getSpawnPoint(Color.BLUE);

        ArrayList<GameCharacter> testList1 = new ArrayList<GameCharacter>();
        testList1.add(player1.getGameCharacter());

        assertEquals(testGb.getMap().getSpawnPoint(Color.BLUE).getCharacters(),testList1);
        assertEquals(testGb.getMap().getSpawnPoint(Color.BLUE),player1.getGameCharacter().getPosition());

        player1.move(Directions.SOUTH);
        currentSquare = currentSquare.getsSquare();

        assertEquals(currentSquare.getCharacters(),testList1);
        assertEquals(currentSquare,player1.getGameCharacter().getPosition());

        player1.move(Directions.WEST);
        currentSquare = currentSquare.getwSquare();

        assertEquals(currentSquare.getCharacters(),testList1);
        assertEquals(currentSquare,player1.getGameCharacter().getPosition());

        player1.move(Directions.EAST);
        currentSquare = currentSquare.geteSquare();

        assertEquals(currentSquare.getCharacters(),testList1);
        assertEquals(currentSquare,player1.getGameCharacter().getPosition());

        player1.move(Directions.NORTH);
        currentSquare = currentSquare.getnSquare();

        assertEquals(currentSquare.getCharacters(),testList1);
        assertEquals(currentSquare,player1.getGameCharacter().getPosition());

        testGb.getMap().getSpawnPoint(Color.BLUE).removeCharacter(player1.getGameCharacter());
    }

}