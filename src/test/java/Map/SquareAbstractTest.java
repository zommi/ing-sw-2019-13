package Map;

import Constants.Directions;
import Exceptions.NoSuchSquareException;
import Player.Character;
import Player.Figure;
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
        Map map = new Map(1);
        Character char1 = new Character(Figure.DESTRUCTOR);
        Character char2 = new Character(Figure.BANSHEE);
        Character char3 = new Character(Figure.DOZER);
        Map.getSquareFromXY(0,0).addCharacter(char1);
        Map.getSquareFromXY(1,2).addCharacter(char2);
        Map.getSquareFromXY(2,1).addCharacter(char3);
        assertTrue(Map.getSquareFromXY(0,0).getVisibleCharacters().contains(char1));
        assertTrue(Map.getSquareFromXY(0,0).getVisibleCharacters().contains(char2));
        assertFalse(Map.getSquareFromXY(0,0).getVisibleCharacters().contains(char3));

    }

    @Test
    void getCharactersThroughWalls() throws  NoSuchSquareException{
        Map map = new Map(1);
        Character char1 = new Character(Figure.DESTRUCTOR);
        Character char2 = new Character(Figure.BANSHEE);
        Character char3 = new Character(Figure.DOZER);
        Map.getSquareFromXY(0,1).addCharacter(char1);
        Map.getSquareFromXY(1,1).addCharacter(char2);
        Map.getSquareFromXY(2,1).addCharacter(char3);
        assertTrue(Map.getSquareFromXY(1,1).getCharactersThroughWalls().contains(char1));
        assertTrue(Map.getSquareFromXY(1,1).getCharactersThroughWalls().contains(char3));
        assertTrue(Map.getSquareFromXY(1,1).getCharactersThroughWalls().contains(char2));
        assertTrue(Map.getSquareFromXY(0,1).getCharactersThroughWalls().contains(char2));
        assertFalse(Map.getSquareFromXY(0,0).getCharactersThroughWalls().contains(char2));
    }

    @Test
    void getExactlyOneMovementCharacters() throws NoSuchSquareException{
        Map map = new Map(1);
        Character char1 = new Character(Figure.DESTRUCTOR);
        Character char2 = new Character(Figure.BANSHEE);
        Character char3 = new Character(Figure.DOZER);
        Map.getSquareFromXY(1,0).addCharacter(char1);
        Map.getSquareFromXY(0,1).addCharacter(char2);
        Map.getSquareFromXY(1,3).addCharacter(char3);
        assertTrue(Map.getSquareFromXY(1,1).getExactlyOneMovementCharacters().contains(char1));
        assertFalse(Map.getSquareFromXY(1,1).getExactlyOneMovementCharacters().contains(char2));
        assertFalse(Map.getSquareFromXY(1,1).getExactlyOneMovementCharacters().contains(char3));
        Map.getSquareFromXY(1,0).removeCharacter(char1);
        Map.getSquareFromXY(1,1).addCharacter(char1);
        assertFalse(Map.getSquareFromXY(1,1).getExactlyOneMovementCharacters().contains(char1));


    }

    @Test
    void getUpToOneMovementCharacters() throws NoSuchSquareException{
        Map map = new Map(1);
        Character char1 = new Character(Figure.DESTRUCTOR);
        Character char2 = new Character(Figure.BANSHEE);
        Character char3 = new Character(Figure.DOZER);
        Map.getSquareFromXY(1,0).addCharacter(char1);
        Map.getSquareFromXY(0,1).addCharacter(char2);
        Map.getSquareFromXY(1,3).addCharacter(char3);
        assertTrue(Map.getSquareFromXY(1,1).getUpToOneMovementCharacters().contains(char1));
        assertFalse(Map.getSquareFromXY(1,1).getUpToOneMovementCharacters().contains(char2));
        assertFalse(Map.getSquareFromXY(1,1).getUpToOneMovementCharacters().contains(char3));
        Map.getSquareFromXY(1,0).removeCharacter(char1);
        Map.getSquareFromXY(1,1).addCharacter(char1);
        assertTrue(Map.getSquareFromXY(1,1).getUpToOneMovementCharacters().contains(char1));
    }

    @Test
    void getVisibleRooms() {
    }

    @Test
    void getAdjacentSquares() throws NoSuchSquareException{
        Map map = new Map(1);
        assertTrue(Map.getSquareFromXY(1,2).getAdjacentSquares().contains(Map.getSquareFromXY(1,1)));
        assertTrue(Map.getSquareFromXY(0,0).getAdjacentSquares().contains(Map.getSquareFromXY(0,1)));
        assertTrue(Map.getSquareFromXY(1,2).getAdjacentSquares().contains(Map.getSquareFromXY(1,3)));
        assertFalse(Map.getSquareFromXY(1,2).getAdjacentSquares().contains(Map.getSquareFromXY(2,2)));
    }

    @Test
    void getTwoSquaresInTheSameDirection() throws NoSuchSquareException{
        Map map = new Map(1);
        assertTrue(Map.getSquareFromXY(0,2).getTwoSquaresInTheSameDirection(Directions.SOUTH).contains(Map.getSquareFromXY(1,2)));
        assertFalse(Map.getSquareFromXY(0,2).getTwoSquaresInTheSameDirection(Directions.SOUTH).contains(Map.getSquareFromXY(2,2)));
        assertTrue(Map.getSquareFromXY(0,2).getTwoSquaresInTheSameDirection(Directions.WEST).contains(Map.getSquareFromXY(0,0)));
    }
}