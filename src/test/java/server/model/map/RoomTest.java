package server.model.map;

import constants.Color;
import exceptions.NoSuchSquareException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void getSquares() throws NoSuchSquareException {
        GameMap gameMap = new GameMap(1);
        assertTrue(gameMap.getRoom(Color.BLUE).getSquares().contains(gameMap.getSquare(0,1)));
        assertFalse(gameMap.getRoom(Color.BLUE).getSquares().contains(gameMap.getSquare(1,1)));
        assertTrue(gameMap.getRoom(Color.WHITE).getSquares().contains(gameMap.getSquare(2,1)));
    }
}