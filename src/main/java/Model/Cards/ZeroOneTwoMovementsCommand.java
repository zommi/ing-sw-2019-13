package Model.Cards;

import Model.Map.SquareAbstract;
import Model.Player.Character;

import java.util.ArrayList;

public class ZeroOneTwoMovementsCommand implements Command{

    public ZeroOneTwoMovementsCommand(){

    }

    public ArrayList<ArrayList<Character>> execute(SquareAbstract square) {
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();

        ArrayList<Character> zero;
        ArrayList<Character> one;
        ArrayList<Character> two;
        zero = (ArrayList<Character>) square.getCharacters();

        one = (ArrayList<Character>) square.getExactlyOneMovementCharacters();

        two = (ArrayList<Character>) square.getExactlyTwoMovementsCharacters();

        e.add(zero);
        e.add(one);
        e.add(two);

        return e;
    }
}
