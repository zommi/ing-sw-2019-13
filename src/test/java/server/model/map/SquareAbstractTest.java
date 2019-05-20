package server.model.map;

import constants.Color;
import constants.Direction;
import exceptions.NoSuchSquareException;
import server.model.player.GameCharacter;
import server.model.player.Figure;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SquareAbstractTest {


    @Test
    void addCharacter() {
    }

    @Test
    void removeCharacter() {
    }

    @Test
    void getCharacters() {
    }

    @Test
    void getVisibleCharacters() throws NoSuchSquareException {
        GameMap gameMap = new GameMap(1);
        GameCharacter char1 = new GameCharacter(Figure.DESTRUCTOR);
        GameCharacter char2 = new GameCharacter(Figure.BANSHEE);
        GameCharacter char3 = new GameCharacter(Figure.DOZER);
        gameMap.getSquare(0,0).addCharacter(char1);
        gameMap.getSquare(1,2).addCharacter(char2);
        gameMap.getSquare(2,1).addCharacter(char3);
        assertTrue(gameMap.getSquare(0,0).getVisibleCharacters().contains(char1));
        assertTrue(gameMap.getSquare(0,0).getVisibleCharacters().contains(char2));
        assertFalse(gameMap.getSquare(0,0).getVisibleCharacters().contains(char3));

    }

    @Test
    void getVisibleRooms() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        SquareAbstract square1 = gameMap.getSquare(1,2);
        assertTrue(square1.getVisibleRooms().contains(gameMap.getRoom(Color.BLUE)));
        assertTrue(square1.getVisibleRooms().contains(gameMap.getRoom(Color.YELLOW)));
        assertFalse(square1.getVisibleRooms().contains(gameMap.getRoom(Color.RED)));
        assertFalse(square1.getVisibleRooms().contains(gameMap.getRoom(Color.WHITE)));
    }

    @Test
    void getVisibleSquares() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        assertTrue(gameMap.getSquare(1,2).getVisibleSquares().contains(gameMap.getSquare(1,1)));
        assertTrue(gameMap.getSquare(1,2).getVisibleSquares().contains(gameMap.getSquare(0,2)));
        assertTrue(gameMap.getSquare(1,2).getVisibleSquares().contains(gameMap.getSquare(0,0)));
        assertTrue(gameMap.getSquare(1,2).getVisibleSquares().contains(gameMap.getSquare(1,3)));
        assertTrue(gameMap.getSquare(1,2).getVisibleSquares().contains(gameMap.getSquare(2,3)));
        assertFalse(gameMap.getSquare(1,2).getVisibleSquares().contains(gameMap.getSquare(2,2)));
    }

    @Test
    void testDistance() throws NoSuchSquareException{
        GameMap map = new GameMap(1);
        SquareAbstract square1 = map.getSquare(0,0);
        SquareAbstract square2 = map.getSquare(0,1);
        SquareAbstract square3 = map.getSquare(2,3);

        assertEquals(0,square1.distance(square1));
        assertEquals(1,square1.distance(square2));
        assertEquals(5,square1.distance(square3));

        SquareAbstract square4 = map.getSquare(0,1);
        SquareAbstract square5 = map.getSquare(2,2);

        assertEquals(5, square4.distance(square5));

    }

    @Test
    void getSquareAtDistanceTest() throws NoSuchSquareException{
        GameMap map = new GameMap(1);
        SquareAbstract square1 = map.getSquare(0,0);

        List<SquareAbstract> testList = new ArrayList<>();
        testList.add(map.getSquare(0,1));
        testList.add(map.getSquare(1,0));

        assertTrue(testList.containsAll(square1.getSquaresAtDistance(1)));

        testList.clear();
        testList.add(map.getSquare(0,2));
        testList.add(map.getSquare(1,1));

        assertTrue(testList.containsAll(square1.getSquaresAtDistance(2)));

        testList.clear();
        testList.add(map.getSquare(1,2));
        testList.add(map.getSquare(2,1));

        assertTrue(testList.containsAll(square1.getSquaresAtDistance(3)));

        testList.clear();

        assertTrue(testList.containsAll(square1.getSquaresAtDistance(10)));

        testList.add(square1);
        assertTrue(testList.containsAll(square1.getSquaresAtDistance(0)));

    }
}