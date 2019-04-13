package Map;

import Constants.Color;
import Exceptions.NoSuchSquareException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Test
    public void isMapConstructorOk() throws NoSuchSquareException {
        Map map = new Map(1);
        List<SpawnPoint> spawnPoints = new ArrayList<>();
        spawnPoints.add(new SpawnPoint(0,3, Color.BLUE));
        spawnPoints.add(new SpawnPoint(1,0, Color.RED));
        spawnPoints.add(new SpawnPoint(3,4, Color.YELLOW));
        assertEquals(spawnPoints.get(0).getColor(), map.getSpawnPoints().get(0).getColor());
        assertTrue(Map.getSquareFromXY(2,3) instanceof SpawnPoint);
        assertEquals(Map.getSquareFromXY(1,0).getColor(), Color.RED);
        assertEquals(Map.getSquareFromXY(2,1).getColor(), Color.WHITE);
        assertTrue(Map.getSquareFromXY(2,1) instanceof Square);

    }



    @Test
    void getRooms() {
    }

    @Test
    void getSpawnPoints() {
    }

    @Test
    void getSquareFromXY() throws NoSuchSquareException{
        Map map = new Map(1);
        assertTrue(Map.getSquareFromXY(1,0).getColor() == Color.RED);
    }

    @Test
    void getSquaresWithSameX() throws NoSuchSquareException{
        Map map = new Map(1);
        assertTrue(Map.getSquaresWithSameX(Map.getSquareFromXY(1,2)).contains(Map.getSquareFromXY(1,0)));
        assertTrue(Map.getSquaresWithSameX(Map.getSquareFromXY(1,2)).contains(Map.getSquareFromXY(1,1)));
        assertTrue(Map.getSquaresWithSameX(Map.getSquareFromXY(1,2)).contains(Map.getSquareFromXY(1,3)));
        assertFalse(Map.getSquaresWithSameX(Map.getSquareFromXY(1,2)).contains(Map.getSquareFromXY(2,3)));
    }

    @Test
    void getSquaresWithSameY() throws NoSuchSquareException{
        Map map = new Map(1);
        assertTrue(Map.getSquaresWithSameY(Map.getSquareFromXY(1,1)).contains(Map.getSquareFromXY(0,1)));
        assertTrue(Map.getSquaresWithSameY(Map.getSquareFromXY(1,1)).contains(Map.getSquareFromXY(2,1)));
        assertFalse(Map.getSquaresWithSameX(Map.getSquareFromXY(1,1)).contains(Map.getSquareFromXY(0,0)));
    }
}