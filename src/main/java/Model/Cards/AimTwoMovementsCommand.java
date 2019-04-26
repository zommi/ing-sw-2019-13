package Model.Cards;

import Model.Map.SquareAbstract;
import Model.Player.Character;

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

        ArrayList<Character> e1 = (ArrayList<Character>)square.getExactlyTwoMovementsCharacters();

        ArrayList<Character> e2 = (ArrayList<Character>) square.getVisibleCharacters();

        for (Character t : (ArrayList<Character>)e1.clone()) {
            if(! e2.contains(t))
                e1.remove(t);
        }

        e.add(e1);
        return e;
    }


}