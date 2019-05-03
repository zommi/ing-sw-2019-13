package server.model.player;

import org.junit.jupiter.api.Test;
import server.model.gameboard.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerHandTest {

    @Test
    public void testPlayCard(){
        ConcretePlayer p = new ConcretePlayer("pippo",new GameBoard(2,5), Figure.DESTRUCTOR);
        assertEquals(Figure.DESTRUCTOR, p.getCharacter().getFigure());
        PlayerHand ph = new PlayerHand(p);


    }
    @Test
    public void test(){
        assertEquals(2,2);
    }
}