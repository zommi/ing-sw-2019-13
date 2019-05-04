package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;


public class ExactlyOneMovementCommand implements Command {


    public ExactlyOneMovementCommand() {
    }

    /**
     * Returns the characters that the player shooting can see exactly 1 movement away from him
     * @param square where the player is
     * @return an arraylist of arralist of the characters that the player shooting can see exactly 1 movements away from him
     */
    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square){
        ArrayList<ArrayList<GameCharacter>> e = new ArrayList<ArrayList<GameCharacter>>();
        e.add((ArrayList)square.getExactlyOneMovementCharacters());
        return e;
    }

}