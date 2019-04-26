package Model.GameBoard;

import Model.Cards.PowerupCard;
import Constants.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PowerupDeckTest {

    @Test
    public void initializeTest(){
        PowerupDeck testDeck = new PowerupDeck();
        System.out.println(testDeck);


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
        List<PowerupCard> testHand = new ArrayList<>();

        for(int i = 0; i<15;i++)testDeck.draw();

        List<PowerupCard> copyList = new ArrayList<>();
        copyList.addAll(testDeck.getDeck());

        assertTrue(copyList.containsAll(testDeck.getDeck()));
        int numberOfCardsInDeck = testDeck.getDeck().size();
        for(int i = 0; i < numberOfCardsInDeck ; i++) {
            testHand.add(testDeck.draw());
        }
        for(PowerupCard card : testHand)card.discard();
        assertTrue(copyList.containsAll(testDeck.getDiscardedCards()));

        assertTrue(testDeck.isEmpty());
        testDeck.restore();

        assertTrue(copyList.containsAll(testDeck.getDeck()));
    }

    @Test
    public void testRestoreAfterDraw(){
        PowerupDeck testDeck = new PowerupDeck();
        List<PowerupCard> testHand = new ArrayList<>();

        for(int i = 0; i<15;i++)testDeck.draw();

        List<PowerupCard> copyList = new ArrayList<>();
        copyList.addAll(testDeck.getDeck());

        assertTrue(copyList.containsAll(testDeck.getDeck()));
        int numberOfCardsInDeck = testDeck.getDeck().size();
        for(int i = 0; i < numberOfCardsInDeck ; i++) {
            testHand.add(testDeck.draw());
        }
        for(PowerupCard card : testHand)card.discard();
        assertTrue(copyList.containsAll(testDeck.getDiscardedCards()));

        assertTrue(testDeck.isEmpty());

        PowerupCard drawnCard = testDeck.draw();
        copyList.remove(drawnCard);
        assertTrue(copyList.containsAll(testDeck.getDeck()));
    }

}