package server.model.cards;

import java.util.*;
import server.model.map.*;
import server.model.player.GameCharacter;

public class AimSeenCommand implements Command {

    public AimSeenCommand(){
    }

    /**
     * Returns the characters that the player shooting can see
     * @param square where the player is
     * @return  an arraylist of arralist of the characters that the player shooting can see
     */
    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square){
        ArrayList<ArrayList<GameCharacter>> e = new ArrayList<ArrayList<GameCharacter>>();
        e.add((ArrayList<GameCharacter>)square.getVisibleCharacters());

        return e;
    }
}