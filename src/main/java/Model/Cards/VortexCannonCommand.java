package Model.Cards;
import Model.Map.SquareAbstract;
import Model.Player.Character;

import java.util.ArrayList;


public class VortexCannonCommand implements Command {


    public VortexCannonCommand() {
    }

    /**
     * Returns the characters that are in "the vortex" or 1 move away from it
     * @param square that the player can see but not his own, called "the vortex"
     * @return an arraylist of arralist of characters that are in "the vortex" or 1 move away from it
     */

    public ArrayList<ArrayList<Character>> execute(SquareAbstract square) { //in this case the square is NOT THE SQUARE OF THE PERSON SHOOTING, BUT THE SQUARE OF THE FIRST PERSON CHOSEN. SEE THE RULES OF THE WEAPONS PLEASE
        ArrayList<Character> e = (ArrayList<Character>)square.getCharacters(); //the characters in the same square
        //union between the same square and the ones distant 1 movement

        ArrayList<Character> e1 = (ArrayList<Character>)square.getExactlyOneMovementCharacters();

        e.addAll(e1);

        ArrayList<ArrayList<Character>> result = new ArrayList<ArrayList<Character>>();
        result.add(e);

        return result;
    }
}

