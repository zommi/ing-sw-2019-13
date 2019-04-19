package Map;

import Constants.Color;
import Exceptions.NoSuchSquareException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void getSquares() throws NoSuchSquareException {
        new Map(1);
        assertTrue(Map.getRoom(Color.BLUE).getSquares().contains(Map.getSquare(0,1)));
        assertFalse(Map.getRoom(Color.BLUE).getSquares().contains(Map.getSquare(1,1)));
        assertTrue(Map.getRoom(Color.WHITE).getSquares().contains(Map.getSquare(2,1)));
    }
}