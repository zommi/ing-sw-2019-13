package Cards;

import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;


public class ExactlyOneMovementCommand implements Command {


    public ExactlyOneMovementCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add((ArrayList)square.getExactlyOneMovementCharacters());
        return e;
    }

}