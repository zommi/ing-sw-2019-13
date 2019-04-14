package Cards;

import Cards.AimInvisibleCommand;
import Map.Map;
import Map.Square;
import Player.Character;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AimInvisibleCommandTest {
    @Test
    void execute ()  throws Exceptions.NoSuchSquareException{ //execute returns an arraylist of arraylist of non visible char

        Character c = new Character(Player.Figure.DESTRUCTOR);
        Character c1 = new Character(Player.Figure.BANSHEE);
        Character c2 = new Character(Player.Figure.DOZER);
        Character c3 = new Character(Player.Figure.VIOLET);
        Character c4 = new Character(Player.Figure.SPROG);

        Map map = new Map(1);

        Map.getSquareFromXY(0,0).addCharacter(c1);
        Map.getSquareFromXY(1,0).addCharacter(c2);
        Map.getSquareFromXY(1,2).addCharacter(c);
        Map.getSquareFromXY(2,2).addCharacter(c3);
        Map.getSquareFromXY(3,1).addCharacter(c4);


        ArrayList<Character> e = new ArrayList<Character>();
        e.add(new Character(Player.Figure.DESTRUCTOR));
        e.add(new Character(Player.Figure.VIOLET));
        e.add(new Character(Player.Figure.SPROG));

        ArrayList<ArrayList<Character>> e1 = new ArrayList<ArrayList<Character>>();
        e1.add(e);

        AimInvisibleCommand aim = new AimInvisibleCommand();
        assertEquals(aim.execute(new Square(0,0,'b')), e1);
    }
}
