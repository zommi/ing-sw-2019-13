package server.model.gameboard;

import org.junit.jupiter.api.Test;
import server.model.cards.AmmoTile;

import static org.junit.jupiter.api.Assertions.*;

class AmmoTileDeckTest {

    @Test
    public void creatorTest(){
        AmmoTileDeck testDeck = new AmmoTileDeck();

        assertEquals(36, testDeck.getDeck().size());

        System.out.println(testDeck);
    }

    @Test
    public void multipleDecksAreEqualTest(){
        AmmoTileDeck testDeck = new AmmoTileDeck();
        AmmoTileDeck testDeck2 = new AmmoTileDeck();


        assertTrue(testDeck.getDeck().containsAll(testDeck2.getDeck()));

    }

    @Test
    public void infiniteDrawTest(){
        AmmoTileDeck deck = new AmmoTileDeck();
        int size = deck.getDeck().size() * 10;
        for(int i = 0 ; i < size ; i++){
            assertTrue(deck.draw() != null);
        }
    }

}