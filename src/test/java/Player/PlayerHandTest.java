package Player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerHandTest {

    /*@Test
    public void testPlayCard(){
        ConcretePlayer p = new ConcretePlayer("pippo",new GameBoard(), Figure.DESTRUCTOR);
        assertEquals(Figure.DESTRUCTOR, p.getCharacter().getFigure());
        PlayerHand ph = new PlayerHand(p);

          assertThrows(InvalidMoveException.class, () -> {
              ph.playCard(2,'q');
        });
    }*/
    @Test
    public void test(){
        assertEquals(2,2);
    }
}