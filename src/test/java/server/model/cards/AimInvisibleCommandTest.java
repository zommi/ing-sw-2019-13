package server.model.cards;

import server.model.map.GameMap;
import server.model.player.Character;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AimInvisibleCommandTest {
    @Test
    void execute ()  throws exceptions.NoSuchSquareException{ //execute returns an arraylist of arraylist of non visible char

        Character c = new Character(server.model.player.Figure.DESTRUCTOR);
        Character c1 = new Character(server.model.player.Figure.BANSHEE);
        Character c2 = new Character(server.model.player.Figure.DOZER);
        Character c3 = new Character(server.model.player.Figure.VIOLET);
        Character c4 = new Character(server.model.player.Figure.SPROG);

        GameMap gameMap = new GameMap(1);

        GameMap.getSquare(0,0).addCharacter(c1);
        GameMap.getSquare(0,1).addCharacter(c2);
        GameMap.getSquare(1,2).addCharacter(c);
        GameMap.getSquare(2,2).addCharacter(c3);
        GameMap.getSquare(1,3).addCharacter(c4);


        ArrayList<Character> e = new ArrayList<Character>();
        e.add(new Character(server.model.player.Figure.DESTRUCTOR));
        e.add(new Character(server.model.player.Figure.VIOLET));
        e.add(new Character(server.model.player.Figure.SPROG));

        ArrayList<ArrayList<Character>> e1 = new ArrayList<ArrayList<Character>>();
        e1.add(e);

        AimInvisibleCommand aim = new AimInvisibleCommand();
        assertEquals(aim.execute(GameMap.getSquare(0,0)), e1);
    }
}
