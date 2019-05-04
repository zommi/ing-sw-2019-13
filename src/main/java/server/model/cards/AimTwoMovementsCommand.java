package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;


public class AimTwoMovementsCommand implements Command {

    public AimTwoMovementsCommand() {
    }

    /**
     * Returns the characters that the player shooting can see 2 movements away from him
     * @param square where the player is
     * @return an arraylist of arralist of the characters that the player shooting can see 2 movements away from him
     */
    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square){
        ArrayList<ArrayList<GameCharacter>> e = new ArrayList<ArrayList<GameCharacter>>();

        ArrayList<GameCharacter> e1 = (ArrayList<GameCharacter>)square.getExactlyTwoMovementsCharacters();

        ArrayList<GameCharacter> e2 = (ArrayList<GameCharacter>) square.getVisibleCharacters();

        for (GameCharacter t : (ArrayList<GameCharacter>)e1.clone()) {
            if(! e2.contains(t))
                e1.remove(t);
        }

        e.add(e1);
        return e;
    }


}