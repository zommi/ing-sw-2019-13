package Player;

import Constants.Color;
import Game.Game;
import GameBoard.GameBoard;
import Map.Map;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ConcretePlayerTest {
    //TODO when map and cards are somewhat done

    @Test
    public void testCreator(){
        GameBoard testGb = GameBoard.instance(1,8);
        PlayerAbstract player1 = new ConcretePlayer("Pippo", testGb, Figure.DESTRUCTOR);
        PlayerAbstract player2 = new ConcretePlayer("Pluto", testGb, Figure.BANSHEE);


        assertNotEquals(player1.getId(),player2.getId());

        player1.spawn(Map.getSpawnPoint(Color.BLUE));
        player2.spawn(Map.getSpawnPoint(Color.YELLOW));
        assertEquals(Map.getSpawnPoint(Color.BLUE), Collections.singleton(player1));
        assertEquals(Map.getSpawnPoint(Color.YELLOW), Collections.singleton(player2));
    }

}