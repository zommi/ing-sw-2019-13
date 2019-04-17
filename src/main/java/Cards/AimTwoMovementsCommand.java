package Cards;

import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;


public class AimTwoMovementsCommand implements Command {

    public AimTwoMovementsCommand() {
    }

    /**
     * Returns the characters that the player shooting can see 2 movements away from him
     * @param square where the player is
     * @return an arraylist of arralist of the characters that the player shooting can see 2 movements away from him
     */
    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add((ArrayList<Character>)square.getUpToOneMovementCharacters());

        return e;
    }


}