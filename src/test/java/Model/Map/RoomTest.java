package Model.Map;

import Constants.Color;
import Exceptions.NoSuchSquareException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void getSquares() throws NoSuchSquareException {
        new GameMap(1);
        assertTrue(GameMap.getRoom(Color.BLUE).getSquares().contains(GameMap.getSquare(0,1)));
        assertFalse(GameMap.getRoom(Color.BLUE).getSquares().contains(GameMap.getSquare(1,1)));
        assertTrue(GameMap.getRoom(Color.WHITE).getSquares().contains(GameMap.getSquare(2,1)));
    }
}