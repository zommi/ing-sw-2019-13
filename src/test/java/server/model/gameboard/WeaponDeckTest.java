package server.model.gameboard;

import constants.*;
import server.model.cards.WeaponCard;
import server.model.map.*;
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
        GameMap testGameMap = new GameMap(1);

        System.out.println(testDeck);

        testDeck.shuffle();

        System.out.println(testDeck);

        WeaponCard firstCard = testDeck.getTop();

        testGameMap.getSpawnPoint(Color.BLUE).addItem(testDeck.draw());
        assertEquals(testGameMap.getSpawnPoint(Color.BLUE).getWeaponCards().get(0).getName(),firstCard.getName());

        testGameMap.getSpawnPoint(Color.BLUE).removeItem(firstCard);

        System.out.println(testDeck);

    }
}