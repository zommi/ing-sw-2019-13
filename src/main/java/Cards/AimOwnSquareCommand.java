package Cards;

import java.lang.reflect.Array;
import java.util.*;
import Map.*;
/**
 * 
 */
public class AimOwnSquareCommand implements Command {


    public AimOwnSquareCommand() {
    }

    @Override
    public ArrayList<ArrayList<Character>> execute(Square square) {
        return null;
    }

    /*
    public ArrayList<ArrayList<Character>> execute(Square square){  //this method returns the arraylist of arraylist of characters that the player can shoot
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add(square.getCharacters());
        return (ArrayList<ArrayList<Character>>) e;
    };
    */
}



