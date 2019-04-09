package Cards;

import java.util.*;
import Map.*;
import Player.Character;


public class ExactlyOneMovementCommand implements Command {


    public ExactlyOneMovementCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(Square square){
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add((ArrayList)square.getExactlyOneMovementCharacters());
        return e;
    }

}