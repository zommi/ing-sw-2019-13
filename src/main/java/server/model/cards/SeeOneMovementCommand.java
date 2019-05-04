package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;

public class SeeOneMovementCommand implements Command{
    public SeeOneMovementCommand() {

    }

    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square) {

        ArrayList<ArrayList<GameCharacter>> e = new ArrayList<ArrayList<GameCharacter>>();

        ArrayList<GameCharacter> e1 = (ArrayList<GameCharacter>) square.getVisibleCharacters();
        ArrayList<GameCharacter> e2 = (ArrayList<GameCharacter>) square.getAtLeastOneMovementCharacters();

        for (GameCharacter t : (ArrayList<GameCharacter>) e1.clone()) {
            if(!e2.contains(t))
                e1.remove(t);
        }

        //now e1 contains only the characters that are also in e2.

        e.add(e1);
        return e;
    }

}
