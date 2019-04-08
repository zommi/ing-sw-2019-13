package Player;

import GameBoard.GameBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerHandTest {

    @Test
    public void testPlayCard(){
        ConcretePlayer p = new ConcretePlayer("pippo",new GameBoard());
        PlayerHand ph = new PlayerHand(p);

        ph.playCard(2,'w');
        ph.playCard(1,'p');
        ph.playCard(2,'q');
    }

}