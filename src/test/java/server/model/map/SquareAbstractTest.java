package server.model.map;

import constants.Color;
import constants.Directions;
import exceptions.NoSuchSquareException;
import server.model.gameboard.GameBoard;
import server.model.player.GameCharacter;
import server.model.player.Figure;
import org.junit.jupiter.api.Test;

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
    void getCharactersThroughWalls() throws  NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        GameCharacter char1 = new GameCharacter(Figure.DESTRUCTOR);
        GameCharacter char2 = new GameCharacter(Figure.BANSHEE);
        GameCharacter char3 = new GameCharacter(Figure.DOZER);
        gameMap.getSquare(0,1).addCharacter(char1);
        gameMap.getSquare(1,1).addCharacter(char2);
        gameMap.getSquare(2,1).addCharacter(char3);
        assertTrue(gameMap.getSquare(1,1).getCharactersThroughWalls().contains(char1));
        assertTrue(gameMap.getSquare(1,1).getCharactersThroughWalls().contains(char3));
        assertTrue(gameMap.getSquare(1,1).getCharactersThroughWalls().contains(char2));
        assertTrue(gameMap.getSquare(0,1).getCharactersThroughWalls().contains(char2));
        assertFalse(gameMap.getSquare(0,0).getCharactersThroughWalls().contains(char2));
    }

    @Test
    void getExactlyOneMovementCharacters() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        GameCharacter char1 = new GameCharacter(Figure.DESTRUCTOR);
        GameCharacter char2 = new GameCharacter(Figure.BANSHEE);
        GameCharacter char3 = new GameCharacter(Figure.DOZER);
        gameMap.getSquare(1,0).addCharacter(char1);
        gameMap.getSquare(0,1).addCharacter(char2);
        gameMap.getSquare(1,3).addCharacter(char3);
        assertTrue(gameMap.getSquare(1,1).getExactlyOneMovementCharacters().contains(char1));
        assertFalse(gameMap.getSquare(1,1).getExactlyOneMovementCharacters().contains(char2));
        assertFalse(gameMap.getSquare(1,1).getExactlyOneMovementCharacters().contains(char3));
        gameMap.getSquare(1,0).removeCharacter(char1);
        gameMap.getSquare(1,1).addCharacter(char1);
        assertFalse(gameMap.getSquare(1,1).getExactlyOneMovementCharacters().contains(char1));


    }

    @Test
    void getUpToOneMovementCharacters() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        GameCharacter char1 = new GameCharacter(Figure.DESTRUCTOR);
        GameCharacter char2 = new GameCharacter(Figure.BANSHEE);
        GameCharacter char3 = new GameCharacter(Figure.DOZER);
        gameMap.getSquare(1,0).addCharacter(char1);
        gameMap.getSquare(0,1).addCharacter(char2);
        gameMap.getSquare(1,3).addCharacter(char3);
        assertTrue(gameMap.getSquare(1,1).getUpToOneMovementCharacters().contains(char1));
        assertFalse(gameMap.getSquare(1,1).getUpToOneMovementCharacters().contains(char2));
        assertFalse(gameMap.getSquare(1,1).getUpToOneMovementCharacters().contains(char3));
        gameMap.getSquare(1,0).removeCharacter(char1);
        gameMap.getSquare(1,1).addCharacter(char1);
        assertTrue(gameMap.getSquare(1,1).getUpToOneMovementCharacters().contains(char1));
    }

    @Test
    void getTwoSquaresInTheSameDirection() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        assertTrue(gameMap.getSquare(0,2).getTwoSquaresInTheSameDirection(Directions.SOUTH).contains(gameMap.getSquare(1,2)));
        assertFalse(gameMap.getSquare(0,2).getTwoSquaresInTheSameDirection(Directions.SOUTH).contains(gameMap.getSquare(2,2)));
        assertTrue(gameMap.getSquare(0,2).getTwoSquaresInTheSameDirection(Directions.WEST).contains(gameMap.getSquare(0,0)));
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
    void getAdjacentSquares() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        assertTrue(gameMap.getSquare(1,2).getAdjacentSquares().contains(gameMap.getSquare(1,1)));
        assertTrue(gameMap.getSquare(0,0).getAdjacentSquares().contains(gameMap.getSquare(0,1)));
        assertTrue(gameMap.getSquare(1,2).getAdjacentSquares().contains(gameMap.getSquare(1,3)));
        assertFalse(gameMap.getSquare(1,2).getAdjacentSquares().contains(gameMap.getSquare(2,2)));
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
    void getExactlyTwoMovementsSquares() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        assertTrue(gameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(gameMap.getSquare(1,0)));
        assertTrue(gameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(gameMap.getSquare(0,1)));
        assertTrue(gameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(gameMap.getSquare(2,1)));
        assertTrue(gameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(gameMap.getSquare(2,3)));
        assertFalse(gameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(gameMap.getSquare(1,2)));
        assertFalse(gameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(gameMap.getSquare(0,2)));
        //TODO check !exist duplicates
    }

    @Test
    void getExactlyTwoMovementsCharacters() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        GameCharacter char1 = new GameCharacter(Figure.DESTRUCTOR);
        GameCharacter char2 = new GameCharacter(Figure.BANSHEE);
        GameCharacter char3 = new GameCharacter(Figure.DOZER);
        gameMap.getSquare(1,0).addCharacter(char1);
        gameMap.getSquare(0,1).addCharacter(char2);
        gameMap.getSquare(0,1).addCharacter(char3);
        assertTrue(gameMap.getSquare(1,2).getExactlyTwoMovementsCharacters().contains(char1));
        assertTrue(gameMap.getSquare(1,2).getExactlyTwoMovementsCharacters().contains(char2));
        assertTrue(gameMap.getSquare(1,2).getExactlyTwoMovementsCharacters().contains(char3));


    }

    @Test
    void getAtLeastOneMovementCharacters() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        GameCharacter char1 = new GameCharacter(Figure.DESTRUCTOR);
        GameCharacter char2 = new GameCharacter(Figure.BANSHEE);
        GameCharacter char3 = new GameCharacter(Figure.DOZER);
        gameMap.getSquare(1,2).addCharacter(char1);
        gameMap.getSquare(0,2).addCharacter(char2);
        gameMap.getSquare(2,2).addCharacter(char3);
        assertFalse(gameMap.getSquare(1,2).getAtLeastOneMovementCharacters().contains(char1));
        assertTrue(gameMap.getSquare(1,2).getAtLeastOneMovementCharacters().contains(char2));
        assertTrue(gameMap.getSquare(1,2).getAtLeastOneMovementCharacters().contains(char3));

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

    }

}