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
        GameBoard testGb = GameBoard.instance(1,8);
        PlayerAbstract player1 = new ConcretePlayer("Pippo", testGb, Figure.DESTRUCTOR);
        PlayerAbstract player2 = new ConcretePlayer("Pluto", testGb, Figure.BANSHEE);


        assertNotEquals(player1.getId(),player2.getId());

        List<Character> list1 = new ArrayList<Character>();
        list1.add(player1.getCharacter());
        List<Character> list2 = new ArrayList<Character>();
        list2.add(player2.getCharacter());

        player1.spawn(GameMap.getSpawnPoint(Color.BLUE));
        player2.spawn(GameMap.getSpawnPoint(Color.YELLOW));
        assertEquals(GameMap.getSpawnPoint(Color.BLUE).getCharacters(), list1);
        assertEquals(GameMap.getSpawnPoint(Color.YELLOW).getCharacters(), list2);

        GameMap.getSpawnPoint(Color.BLUE).removeCharacter(player1.getCharacter());
        GameMap.getSpawnPoint(Color.YELLOW).removeCharacter(player2.getCharacter());

    }

    @Test
    public void moveTest(){
        GameBoard testGb = GameBoard.instance(1,8);
        PlayerAbstract player1 = new ConcretePlayer("Pippo", testGb, Figure.DESTRUCTOR);

        player1.spawn(GameMap.getSpawnPoint(Color.BLUE));
        SquareAbstract currentSquare = GameMap.getSpawnPoint(Color.BLUE);

        ArrayList<Character> testList1 = new ArrayList<Character>();
        testList1.add(player1.getCharacter());

        assertEquals(GameMap.getSpawnPoint(Color.BLUE).getCharacters(),testList1);
        assertEquals(GameMap.getSpawnPoint(Color.BLUE),player1.getCharacter().getPosition());

        player1.move(Directions.SOUTH);
        currentSquare = currentSquare.getsSquare();

        assertEquals(currentSquare.getCharacters(),testList1);
        assertEquals(currentSquare,player1.getCharacter().getPosition());

        player1.move(Directions.WEST);
        currentSquare = currentSquare.getwSquare();

        assertEquals(currentSquare.getCharacters(),testList1);
        assertEquals(currentSquare,player1.getCharacter().getPosition());

        player1.move(Directions.EAST);
        currentSquare = currentSquare.geteSquare();

        assertEquals(currentSquare.getCharacters(),testList1);
        assertEquals(currentSquare,player1.getCharacter().getPosition());

        player1.move(Directions.NORTH);
        currentSquare = currentSquare.getnSquare();

        assertEquals(currentSquare.getCharacters(),testList1);
        assertEquals(currentSquare,player1.getCharacter().getPosition());

        GameMap.getSpawnPoint(Color.BLUE).removeCharacter(player1.getCharacter());
    }

}