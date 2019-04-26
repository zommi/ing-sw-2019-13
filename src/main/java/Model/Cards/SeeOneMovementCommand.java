package Model.Cards;

import Model.Map.SquareAbstract;
import Model.Player.Character;

import java.util.ArrayList;

public class SeeOneMovementCommand implements Command{
    public SeeOneMovementCommand() {

    }

    public ArrayList<ArrayList<Character>> execute(SquareAbstract square) {

        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();

        ArrayList<Character> e1 = (ArrayList<Character>) square.getVisibleCharacters();
        ArrayList<Character> e2 = (ArrayList<Character>) square.getAtLeastOneMovementCharacters();

        for (Character t : (ArrayList<Character>) e1.clone()) {
            if(!e2.contains(t))
                e1.remove(t);
        }

        //now e1 contains only the characters that are also in e2.

        e.add(e1);
        return e;
    }

}
