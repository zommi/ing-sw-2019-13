package Map;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Test
    public void isMapConstructorOk(){
        Map map = new Map(1);
        List<SpawnPoint> spawnPoints = new ArrayList<>();
        spawnPoints.add(new SpawnPoint(0,3, 'b'));
        spawnPoints.add(new SpawnPoint(1,0, 'r'));
        spawnPoints.add(new SpawnPoint(3,4, 'y'));
        assertEquals(spawnPoints, map.getSpawnPoints());

    }



    @Test
    void getRooms() {
    }

    @Test
    void getSpawnPoints() {
    }

    @Test
    void getSquareFromXY() {
    }

    @Test
    void getSquaresWithSameX() {
    }

    @Test
    void getSquaresWithSameY() {
    }
}