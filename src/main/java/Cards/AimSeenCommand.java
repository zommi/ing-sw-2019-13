package Cards;

import java.util.*;
import Map.*;
import Player.Character;

public class AimSeenCommand implements Command {

    public AimSeenCommand(){
    }

    /**
     * Returns the characters that the player shooting can see
     * @param square where the player is
     * @return  an arraylist of arralist of the characters that the player shooting can see
     */
    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add((ArrayList<Character>)square.getVisibleCharacters());

        return e;
    }
}