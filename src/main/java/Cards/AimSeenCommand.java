package Cards;

import java.util.*;

import Player.Character;

public class AimSeenCommand {

    public AimSeenCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(Map.NoSpawnSquare square){
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add((ArrayList<Character>)square.getVisibleCharacters());

        return e;
    }
}