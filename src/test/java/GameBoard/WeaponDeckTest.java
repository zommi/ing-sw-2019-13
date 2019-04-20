package GameBoard;

import Constants.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponDeckTest {

    @Test
    public void constructorTest(){
        WeaponDeck testDeck = new WeaponDeck();

        assertEquals(Constants.NUMBER_OF_WEAPONS,testDeck.getSize());
        System.out.println(testDeck);
    }
}