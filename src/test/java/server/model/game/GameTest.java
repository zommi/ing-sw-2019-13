package server.model.game;

import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void constructorTest(){
        Game testGame = new Game(1,8, null);

        ConcretePlayer testPlayer1 = new ConcretePlayer("pippo");
        testPlayer1.setPlayerCharacter(Figure.SPROG);
        ConcretePlayer testPlayer2 = new ConcretePlayer("pluto");
        testPlayer2.setPlayerCharacter(Figure.DESTRUCTOR);
        List<ConcretePlayer> testList = new ArrayList<>();

        testList.add(testPlayer1);
        testList.add(testPlayer2);

        testGame.addPlayerToGame(testPlayer1);
        testGame.addPlayerToGame(testPlayer2);

        assertEquals(testList,testGame.getPlayers());

        testGame.removePlayer(testPlayer1);

        testList.remove(testPlayer1);

        assertEquals(testList,testGame.getPlayers());

        //testGame.nextState();

        //assertThrows(WrongGameStateException.class, () ->
        //        testGame.addPlayer(testPlayer1));



    }

}