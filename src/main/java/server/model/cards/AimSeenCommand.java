package server.model.cards;

import java.util.*;
import server.model.map.*;
import server.model.player.Character;

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