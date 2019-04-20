package GameBoard;

import Constants.*;
import Map.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponDeckTest {

    @Test
    public void constructorTest(){
        WeaponDeck testDeck = new WeaponDeck();

        assertEquals(Constants.NUMBER_OF_WEAPONS,testDeck.getSize());
        //System.out.println(testDeck);
    }

    @Test
    public void drawTest(){
        WeaponDeck testDeck = new WeaponDeck();
        Map testMap = new Map(1);

        System.out.println(testDeck);

        testDeck.shuffle();

        System.out.println(testDeck);

        String firstCard = testDeck.getTop();

        testDeck.draw(Map.getSpawnPoint(Color.BLUE));
        assertEquals(Map.getSpawnPoint(Color.BLUE).getWeaponCards().get(0).getName(),firstCard);

        System.out.println(testDeck);

    }
}