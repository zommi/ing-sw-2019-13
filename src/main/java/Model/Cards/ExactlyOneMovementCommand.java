package Model.Cards;

import Model.Map.SquareAbstract;
import Model.Player.Character;

import java.util.ArrayList;


public class ExactlyOneMovementCommand implements Command {


    public ExactlyOneMovementCommand() {
    }

    /**
     * Returns the characters that the player shooting can see exactly 1 movement away from him
     * @param square where the player is
     * @return an arraylist of arralist of the characters that the player shooting can see exactly 1 movements away from him
     */
    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add((ArrayList)square.getExactlyOneMovementCharacters());
        return e;
    }

}