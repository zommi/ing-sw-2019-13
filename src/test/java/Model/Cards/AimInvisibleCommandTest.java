package Model.Cards;

import Model.Map.GameMap;
import Model.Player.Character;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AimInvisibleCommandTest {
    @Test
    void execute ()  throws Exceptions.NoSuchSquareException{ //execute returns an arraylist of arraylist of non visible char

        Character c = new Character(Model.Player.Figure.DESTRUCTOR);
        Character c1 = new Character(Model.Player.Figure.BANSHEE);
        Character c2 = new Character(Model.Player.Figure.DOZER);
        Character c3 = new Character(Model.Player.Figure.VIOLET);
        Character c4 = new Character(Model.Player.Figure.SPROG);

        GameMap gameMap = new GameMap(1);

        GameMap.getSquare(0,0).addCharacter(c1);
        GameMap.getSquare(0,1).addCharacter(c2);
        GameMap.getSquare(1,2).addCharacter(c);
        GameMap.getSquare(2,2).addCharacter(c3);
        GameMap.getSquare(1,3).addCharacter(c4);


        ArrayList<Character> e = new ArrayList<Character>();
        e.add(new Character(Model.Player.Figure.DESTRUCTOR));
        e.add(new Character(Model.Player.Figure.VIOLET));
        e.add(new Character(Model.Player.Figure.SPROG));

        ArrayList<ArrayList<Character>> e1 = new ArrayList<ArrayList<Character>>();
        e1.add(e);

        AimInvisibleCommand aim = new AimInvisibleCommand();
        assertEquals(aim.execute(GameMap.getSquare(0,0)), e1);
    }
}
