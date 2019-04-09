package Cards;

import java.util.*;

import Player.Character;


public class ExactlyOneMovementCommand implements Command {


    public ExactlyOneMovementCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(Map.NoSpawnSquare square){
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add((ArrayList)square.getExactlyOneMovementCharacters());
        return e;
    }

}