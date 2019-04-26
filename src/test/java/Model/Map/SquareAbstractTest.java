package Model.Map;

import Constants.Color;
import Constants.Directions;
import Exceptions.NoSuchSquareException;
import Model.Player.Character;
import Model.Player.Figure;
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
        Character char1 = new Character(Figure.DESTRUCTOR);
        Character char2 = new Character(Figure.BANSHEE);
        Character char3 = new Character(Figure.DOZER);
        GameMap.getSquare(0,0).addCharacter(char1);
        GameMap.getSquare(1,2).addCharacter(char2);
        GameMap.getSquare(2,1).addCharacter(char3);
        assertTrue(GameMap.getSquare(0,0).getVisibleCharacters().contains(char1));
        assertTrue(GameMap.getSquare(0,0).getVisibleCharacters().contains(char2));
        assertFalse(GameMap.getSquare(0,0).getVisibleCharacters().contains(char3));

    }

    @Test
    void getCharactersThroughWalls() throws  NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        Character char1 = new Character(Figure.DESTRUCTOR);
        Character char2 = new Character(Figure.BANSHEE);
        Character char3 = new Character(Figure.DOZER);
        GameMap.getSquare(0,1).addCharacter(char1);
        GameMap.getSquare(1,1).addCharacter(char2);
        GameMap.getSquare(2,1).addCharacter(char3);
        assertTrue(GameMap.getSquare(1,1).getCharactersThroughWalls().contains(char1));
        assertTrue(GameMap.getSquare(1,1).getCharactersThroughWalls().contains(char3));
        assertTrue(GameMap.getSquare(1,1).getCharactersThroughWalls().contains(char2));
        assertTrue(GameMap.getSquare(0,1).getCharactersThroughWalls().contains(char2));
        assertFalse(GameMap.getSquare(0,0).getCharactersThroughWalls().contains(char2));
    }

    @Test
    void getExactlyOneMovementCharacters() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        Character char1 = new Character(Figure.DESTRUCTOR);
        Character char2 = new Character(Figure.BANSHEE);
        Character char3 = new Character(Figure.DOZER);
        GameMap.getSquare(1,0).addCharacter(char1);
        GameMap.getSquare(0,1).addCharacter(char2);
        GameMap.getSquare(1,3).addCharacter(char3);
        assertTrue(GameMap.getSquare(1,1).getExactlyOneMovementCharacters().contains(char1));
        assertFalse(GameMap.getSquare(1,1).getExactlyOneMovementCharacters().contains(char2));
        assertFalse(GameMap.getSquare(1,1).getExactlyOneMovementCharacters().contains(char3));
        GameMap.getSquare(1,0).removeCharacter(char1);
        GameMap.getSquare(1,1).addCharacter(char1);
        assertFalse(GameMap.getSquare(1,1).getExactlyOneMovementCharacters().contains(char1));


    }

    @Test
    void getUpToOneMovementCharacters() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        Character char1 = new Character(Figure.DESTRUCTOR);
        Character char2 = new Character(Figure.BANSHEE);
        Character char3 = new Character(Figure.DOZER);
        GameMap.getSquare(1,0).addCharacter(char1);
        GameMap.getSquare(0,1).addCharacter(char2);
        GameMap.getSquare(1,3).addCharacter(char3);
        assertTrue(GameMap.getSquare(1,1).getUpToOneMovementCharacters().contains(char1));
        assertFalse(GameMap.getSquare(1,1).getUpToOneMovementCharacters().contains(char2));
        assertFalse(GameMap.getSquare(1,1).getUpToOneMovementCharacters().contains(char3));
        GameMap.getSquare(1,0).removeCharacter(char1);
        GameMap.getSquare(1,1).addCharacter(char1);
        assertTrue(GameMap.getSquare(1,1).getUpToOneMovementCharacters().contains(char1));
    }

    @Test
    void getTwoSquaresInTheSameDirection() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        assertTrue(GameMap.getSquare(0,2).getTwoSquaresInTheSameDirection(Directions.SOUTH).contains(GameMap.getSquare(1,2)));
        assertFalse(GameMap.getSquare(0,2).getTwoSquaresInTheSameDirection(Directions.SOUTH).contains(GameMap.getSquare(2,2)));
        assertTrue(GameMap.getSquare(0,2).getTwoSquaresInTheSameDirection(Directions.WEST).contains(GameMap.getSquare(0,0)));
    }

    @Test
    void getVisibleRooms() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        SquareAbstract square1 = GameMap.getSquare(1,2);
        assertTrue(square1.getVisibleRooms().contains(GameMap.getRoom(Color.BLUE)));
        assertTrue(square1.getVisibleRooms().contains(GameMap.getRoom(Color.YELLOW)));
        assertFalse(square1.getVisibleRooms().contains(GameMap.getRoom(Color.RED)));
        assertFalse(square1.getVisibleRooms().contains(GameMap.getRoom(Color.WHITE)));
    }

    @Test
    void getAdjacentSquares() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        assertTrue(GameMap.getSquare(1,2).getAdjacentSquares().contains(GameMap.getSquare(1,1)));
        assertTrue(GameMap.getSquare(0,0).getAdjacentSquares().contains(GameMap.getSquare(0,1)));
        assertTrue(GameMap.getSquare(1,2).getAdjacentSquares().contains(GameMap.getSquare(1,3)));
        assertFalse(GameMap.getSquare(1,2).getAdjacentSquares().contains(GameMap.getSquare(2,2)));
    }

    @Test
    void getVisibleSquares() throws NoSuchSquareException{
        new GameMap(1);
        assertTrue(GameMap.getSquare(1,2).getVisibleSquares().contains(GameMap.getSquare(1,1)));
        assertTrue(GameMap.getSquare(1,2).getVisibleSquares().contains(GameMap.getSquare(0,2)));
        assertTrue(GameMap.getSquare(1,2).getVisibleSquares().contains(GameMap.getSquare(0,0)));
        assertTrue(GameMap.getSquare(1,2).getVisibleSquares().contains(GameMap.getSquare(1,3)));
        assertTrue(GameMap.getSquare(1,2).getVisibleSquares().contains(GameMap.getSquare(2,3)));
        assertFalse(GameMap.getSquare(1,2).getVisibleSquares().contains(GameMap.getSquare(2,2)));
    }

    @Test
    void getExactlyTwoMovementsSquares() throws NoSuchSquareException{
        new GameMap(1);
        assertTrue(GameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(GameMap.getSquare(1,0)));
        assertTrue(GameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(GameMap.getSquare(0,1)));
        assertTrue(GameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(GameMap.getSquare(2,1)));
        assertTrue(GameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(GameMap.getSquare(2,3)));
        assertFalse(GameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(GameMap.getSquare(1,2)));
        assertFalse(GameMap.getSquare(1,2).getExactlyTwoMovementsSquares().contains(GameMap.getSquare(0,2)));
        //TODO check !exist duplicates
    }

    @Test
    void getExactlyTwoMovementsCharacters() throws NoSuchSquareException{
        new GameMap(1);
        Character char1 = new Character(Figure.DESTRUCTOR);
        Character char2 = new Character(Figure.BANSHEE);
        Character char3 = new Character(Figure.DOZER);
        GameMap.getSquare(1,0).addCharacter(char1);
        GameMap.getSquare(0,1).addCharacter(char2);
        GameMap.getSquare(0,1).addCharacter(char3);
        assertTrue(GameMap.getSquare(1,2).getExactlyTwoMovementsCharacters().contains(char1));
        assertTrue(GameMap.getSquare(1,2).getExactlyTwoMovementsCharacters().contains(char2));
        assertTrue(GameMap.getSquare(1,2).getExactlyTwoMovementsCharacters().contains(char3));


    }

    @Test
    void getAtLeastOneMovementCharacters() throws NoSuchSquareException{
        new GameMap(1);
        Character char1 = new Character(Figure.DESTRUCTOR);
        Character char2 = new Character(Figure.BANSHEE);
        Character char3 = new Character(Figure.DOZER);
        GameMap.getSquare(1,2).addCharacter(char1);
        GameMap.getSquare(0,2).addCharacter(char2);
        GameMap.getSquare(2,2).addCharacter(char3);
        assertFalse(GameMap.getSquare(1,2).getAtLeastOneMovementCharacters().contains(char1));
        assertTrue(GameMap.getSquare(1,2).getAtLeastOneMovementCharacters().contains(char2));
        assertTrue(GameMap.getSquare(1,2).getAtLeastOneMovementCharacters().contains(char3));

    }


}