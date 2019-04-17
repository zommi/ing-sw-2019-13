package Player;

import Cards.Bullet;
import Constants.Color;
import GameBoard.GameBoard;
import Items.AmmoCube;
import Map.Map;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PlayerBoardTest {

    @Test
    public void bulletTest(){
        GameBoard gbtest = GameBoard.instance(1,8);
        ConcretePlayer player = new ConcretePlayer("Pippo",gbtest,Figure.SPROG);
        PlayerBoard testPlayerBoard = player.getBoard();

        player.spawn(Map.getSpawnPoint(Color.BLUE));

        Bullet testBullet1 = new Bullet(0,0,3,3,false);
        player.receiveBullet(testBullet1,Color.RED);

        assertEquals(3, testPlayerBoard.getDamageTaken());
        for(int i = 0; i < testPlayerBoard.getDamageTaken(); i++){
            assertEquals(Color.RED,testPlayerBoard.getTokenColor(i));
        }
        assertEquals(3, testPlayerBoard.getMarks().size());
        for(Color c : testPlayerBoard.getMarks()){
            assertEquals(Color.RED,c);
        }

        Bullet testBullet2 = new Bullet(0,0,1,1,false);
        player.receiveBullet(testBullet2,Color.RED);

        assertEquals(7, testPlayerBoard.getDamageTaken());
        for(int i = 3; i < testPlayerBoard.getDamageTaken(); i++){
            assertEquals(Color.RED,testPlayerBoard.getTokenColor(i));
        }
        assertEquals(1, testPlayerBoard.getMarks().size());
        for(Color c : testPlayerBoard.getMarks()){
            assertEquals(Color.RED,c);
        }

        Bullet testBullet3 = new Bullet(0,0,1,3,false);
        player.receiveBullet(testBullet3,Color.BLUE);

        assertEquals(8, testPlayerBoard.getDamageTaken());
        for(int i = 7; i < testPlayerBoard.getDamageTaken(); i++){
            assertEquals(Color.BLUE,testPlayerBoard.getTokenColor(i));
        }
        assertEquals(3, testPlayerBoard.getMarks().size());
        assertEquals(1,testPlayerBoard.getMarksOfAColor(Color.RED));
        assertEquals(2,testPlayerBoard.getMarksOfAColor(Color.BLUE));
    }

}