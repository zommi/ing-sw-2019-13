package Player;

import Exceptions.InvalidMoveException;
import GameBoard.GameBoard;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PlayerHandTest {

    @Test
    public void testPlayCard(){
        ConcretePlayer p = new ConcretePlayer("pippo",GameBoard.istance(2,5), Figure.DESTRUCTOR);
        assertEquals(Figure.DESTRUCTOR, p.getCharacter().getFigure());
        PlayerHand ph = new PlayerHand(p);

          assertThrows(InvalidMoveException.class, () -> {
              ph.playCard(2,'q');
        });
    }
    @Test
    public void test(){
        assertEquals(2,2);
    }
}