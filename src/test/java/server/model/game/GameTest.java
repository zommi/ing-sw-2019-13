package server.model.game;

import exceptions.WrongGameStateException;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void constructorTest(){
        Game testGame = new Game(1,8);

        ConcretePlayer testPlayer1 = new ConcretePlayer("pippo");
        testPlayer1.setPlayerCharacter(Figure.SPROG);
        ConcretePlayer testPlayer2 = new ConcretePlayer("pluto");
        testPlayer2.setPlayerCharacter(Figure.DESTRUCTOR);
        List<ConcretePlayer> testList = new ArrayList<ConcretePlayer>();

        testList.add(testPlayer1);
        testList.add(testPlayer2);
        try{

            testGame.addPlayer(testPlayer1);
            testGame.addPlayer(testPlayer2);

            assertEquals(testList,testGame.getActivePlayers());

            testGame.removePlayer(testPlayer1);

            testList.remove(testPlayer1);

            assertEquals(testList,testGame.getActivePlayers());

            testGame.nextState();

            //assertThrows(WrongGameStateException.class, () ->
            //        testGame.addPlayer(testPlayer1));

        }catch (WrongGameStateException e){
            e.printStackTrace();
        }

    }

}