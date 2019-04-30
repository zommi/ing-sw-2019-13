package server.model.map;

import constants.Color;
import exceptions.NoSuchSquareException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

    @Test
    public void isMapConstructorOk() throws NoSuchSquareException {
        GameMap gameMap = new GameMap(1);
        List<SpawnPoint> spawnPoints = new ArrayList<>();
        spawnPoints.add(new SpawnPoint(0,3, Color.BLUE));
        spawnPoints.add(new SpawnPoint(1,0, Color.RED));
        spawnPoints.add(new SpawnPoint(3,4, Color.YELLOW));
        assertEquals(spawnPoints.get(0).getColor(), gameMap.getSpawnPoints().get(0).getColor());
        assertTrue(GameMap.getSquare(2,3) instanceof SpawnPoint);
        assertEquals(GameMap.getSquare(1,0).getColor(), Color.RED);
        assertEquals(GameMap.getSquare(2,1).getColor(), Color.WHITE);
        assertTrue(GameMap.getSquare(2,1) instanceof Square);

    }



    @Test
    void getRooms() {
        new GameMap(1);
        List<Room> roomList = GameMap.getRooms();
        assertTrue(roomList.get(0).getColor() == Color.BLUE);
        assertTrue(roomList.get(1).getColor() == Color.RED);
        assertTrue(roomList.get(2).getColor() == Color.YELLOW);
        assertTrue(roomList.get(3).getColor() == Color.WHITE);

    }

    @Test
    void getSpawnPoints() {
        GameMap gameMap = new GameMap(1);
        ArrayList<SpawnPoint> sp = (ArrayList<SpawnPoint>) GameMap.getSpawnPoints();
        assertTrue(GameMap.getSpawnPoint(Color.RED).getColor() == sp.get(1).getColor());
    }

    @Test
    void getSquareFromXY() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        assertTrue(GameMap.getSquare(1,0).getColor() == Color.RED);
    }

    @Test
    void getSquaresWithSameX() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        assertTrue(GameMap.getSquaresWithSameRow(GameMap.getSquare(1,2)).contains(GameMap.getSquare(1,0)));
        assertTrue(GameMap.getSquaresWithSameRow(GameMap.getSquare(1,2)).contains(GameMap.getSquare(1,1)));
        assertTrue(GameMap.getSquaresWithSameRow(GameMap.getSquare(1,2)).contains(GameMap.getSquare(1,3)));
        assertFalse(GameMap.getSquaresWithSameRow(GameMap.getSquare(1,2)).contains(GameMap.getSquare(2,3)));
    }

    @Test
    void getSquaresWithSameY() throws NoSuchSquareException{
        GameMap gameMap = new GameMap(1);
        assertTrue(GameMap.getSquaresWithSameCol(GameMap.getSquare(1,1)).contains(GameMap.getSquare(0,1)));
        assertTrue(GameMap.getSquaresWithSameCol(GameMap.getSquare(1,1)).contains(GameMap.getSquare(2,1)));
        assertFalse(GameMap.getSquaresWithSameRow(GameMap.getSquare(1,1)).contains(GameMap.getSquare(0,0)));
    }
}