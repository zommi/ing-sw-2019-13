package GameBoard;

import Cards.PowerupCard;
import Constants.Color;
import Exceptions.InvalidMoveException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PowerupDeckTest {

    @Test
    public void initializeTest(){
        PowerupDeck testDeck = new PowerupDeck();
        //System.out.println(testDeck);


        long redCardsCounter = testDeck.getDeck().stream()
                .filter(card -> Color.RED.equals(card.getColor()))
                .count();

        long blueCardsCounter = testDeck.getDeck().stream()
                .filter(card -> Color.BLUE.equals(card.getColor()))
                .count();

        long yellowCardsCounter = testDeck.getDeck().stream()
                .filter(card -> Color.YELLOW.equals(card.getColor()))
                .count();

        assertEquals(8,redCardsCounter);
        assertEquals(8,blueCardsCounter);
        assertEquals(8,yellowCardsCounter);

    }

    @Test
    public void testRestore(){
        PowerupDeck testDeck = new PowerupDeck();

        for(int i = 0; i < 15 ; i++) testDeck.draw(); //random number of draws

        List<PowerupCard> copyList = new ArrayList<>();
        copyList.addAll(testDeck.getDeck());

        assertTrue(copyList.containsAll(testDeck.getDeck()));
        for(PowerupCard card : testDeck.getDeck()) card.discard();
        assertTrue(copyList.containsAll(testDeck.getDiscardedCards()));

        testDeck.restore();
        assertTrue(copyList.containsAll(testDeck.getDeck()));
    }

    @Test
    public void testRestoreAfterDraw(){
        PowerupDeck testDeck = new PowerupDeck();

        for(int i = 0; i < 15 ; i++) testDeck.draw(); //random number of draws

        List<PowerupCard> copyList = new ArrayList<>();
        copyList.addAll(testDeck.getDeck());

        assertTrue(copyList.containsAll(testDeck.getDeck()));
        for(PowerupCard card : testDeck.getDeck()) card.discard();
        assertTrue(copyList.containsAll(testDeck.getDiscardedCards()));

        PowerupCard drawnCard = testDeck.draw();
        copyList.remove(drawnCard);
        assertTrue(copyList.containsAll(testDeck.getDeck()));
    }

}