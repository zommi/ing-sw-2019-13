package Cards;

import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;


public class AimOwnSquareCommand implements Command {


    public AimOwnSquareCommand() {
    }


    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){  //this method returns the arraylist of arraylist of characters that the player can shoot
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add((ArrayList)square.getCharacters());
        return e;
    }
}



