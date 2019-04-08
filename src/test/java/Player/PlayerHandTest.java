package Player;

import Exceptions.InvalidMoveException;
import GameBoard.GameBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerHandTest {

    @Test
    public void testPlayCard(){
        ConcretePlayer p = new ConcretePlayer("pippo",new GameBoard());
        PlayerHand ph = new PlayerHand(p);

        assertThrows(InvalidMoveException.class, () -> {
                    ph.playCard(2,'q');
        });
    }

    /*
    @Test
    public void test(){
        assertEquals(2,2);
    }
    */
}