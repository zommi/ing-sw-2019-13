package server.model.cards;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;


public class VortexCannonCommand implements Command {


    public VortexCannonCommand() {
    }

    /**
     * Returns the characters that are in "the vortex" or 1 move away from it
     * @param square that the player can see but not his own, called "the vortex"
     * @return an arraylist of arralist of characters that are in "the vortex" or 1 move away from it
     */

    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square) { //in this case the square is NOT THE SQUARE OF THE PERSON SHOOTING, BUT THE SQUARE OF THE FIRST PERSON CHOSEN. SEE THE RULES OF THE WEAPONS PLEASE
        ArrayList<GameCharacter> e = (ArrayList<GameCharacter>)square.getCharacters(); //the characters in the same square
        //union between the same square and the ones distant 1 movement

        ArrayList<GameCharacter> e1 = (ArrayList<GameCharacter>)square.getExactlyOneMovementCharacters();

        e.addAll(e1);

        ArrayList<ArrayList<GameCharacter>> result = new ArrayList<ArrayList<GameCharacter>>();
        result.add(e);

        return result;
    }
}

