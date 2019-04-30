package server.model.gameboard;

import org.junit.jupiter.api.Test;

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

}