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
    void execute() { //execute returns an arraylist of arraylist of non visible char

        Character c = new Character(Player.Figure.DESTRUCTOR);
        Character c1 = new Character(Player.Figure.BANSHEE);
        Character c2 = new Character(Player.Figure.DOZER);
        Character c3 = new Character(Player.Figure.VIOLET);
        Character c4 = new Character(Player.Figure.SPROG);

        Map map = new Map(1);

        Square square0 = new Square(0,0, 'b');
        square0.addCharacter(new Character(Player.Figure.BANSHEE));
        Square square1 = new Square(1,0, 'b');
        square1.addCharacter(new Character(Player.Figure.DOZER));
        Square square2 = new Square(2,0, 'B');
        Square square3 = new Square(0,1, 'R' );
        Square square4 = new Square(1,1, 'r');
        Square square5 = new Square(2,1, 'r');
        Square square6 = new Square(3,1, 'y');
        Square square7 = new Square(1,2, 'w');
        Square square8 = new Square(2,2, 'w');
        Square square9 = new Square(3,2, 'Y');
        square7.addCharacter(new Character(Player.Figure.DESTRUCTOR));
        square8.addCharacter(new Character(Player.Figure.VIOLET));
        square9.addCharacter(new Character(Player.Figure.SPROG));


        ArrayList<Character> e = new ArrayList<Character>();
        e.add(new Character(Player.Figure.DESTRUCTOR));
        e.add(new Character(Player.Figure.VIOLET));
        e.add(new Character(Player.Figure.SPROG));

        ArrayList<ArrayList<Character>> e1 = new ArrayList<ArrayList<Character>>();
        e1.add(e);

        AimInvisibleCommand aim = new AimInvisibleCommand();
        assertEquals(aim.execute(new Square(0,0,'b')), e);
    }
}
