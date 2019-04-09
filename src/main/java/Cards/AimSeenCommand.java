package Cards;

import java.util.*;
import Map.*;
import Player.Character;

public class AimSeenCommand {

    public AimSeenCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(Square square){
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add((ArrayList<Character>)square.getVisibleCharacters());

        return e;
    }
}