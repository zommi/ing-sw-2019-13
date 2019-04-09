package Cards;
import java.lang.reflect.Array;
import java.util.*;
import Map.*;
import Player.Character;


public class FlameThrowerCommand {

    private char direction;

    public FlameThrowerCommand() {
    }

    public void setdirection(char c) //i need this method in order to communicate the direction from the controller to the model
    {
        this.direction = c;
    }

    public ArrayList<ArrayList<Character>> execute(Square square) {

        ArrayList<Square> eN = (ArrayList<Square>)square.getTwoSquaresInTheSameDirection('n');
        ArrayList<Square> eS = (ArrayList<Square>)square.getTwoSquaresInTheSameDirection('s');
        ArrayList<Square> eW = (ArrayList<Square>)square.getTwoSquaresInTheSameDirection('w');
        ArrayList<Square> eE = (ArrayList<Square>)square.getTwoSquaresInTheSameDirection('e');

        ArrayList<ArrayList<Character>> cN = new ArrayList<ArrayList<Character>>();
        ArrayList<ArrayList<Character>> cS = new ArrayList<ArrayList<Character>>();
        ArrayList<ArrayList<Character>> cW = new ArrayList<ArrayList<Character>>();
        ArrayList<ArrayList<Character>> cE = new ArrayList<ArrayList<Character>>();


        for (Square t : eN) {
            cN.add((ArrayList<Character>)t.getCharacters());
        }

        for (Square t : eS) {
            cS.add((ArrayList<Character>)t.getCharacters());
        }

        for (Square t : eW) {
            cW.add((ArrayList<Character>)t.getCharacters());
        }

        for (Square t : eE) {
            cE.add((ArrayList<Character>)t.getCharacters());
        }

        if(this.direction == 'n')
            return (ArrayList<ArrayList<Character>>)cN.clone();
        else if(this.direction == 's')
            return (ArrayList<ArrayList<Character>>)cS.clone();
        else if(this.direction == 'e')
            return (ArrayList<ArrayList<Character>>)cE.clone();
        else if(this.direction == 'w')
            return (ArrayList<ArrayList<Character>>)cW.clone();

        //now the user has to say to me which direction he wants. Otherwise I don't know what to return.

    }

}
