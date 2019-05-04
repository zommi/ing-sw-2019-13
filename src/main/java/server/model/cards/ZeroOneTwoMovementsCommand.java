package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;

public class ZeroOneTwoMovementsCommand implements Command{

    public ZeroOneTwoMovementsCommand(){

    }

    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square) {
        ArrayList<ArrayList<GameCharacter>> e = new ArrayList<ArrayList<GameCharacter>>();

        ArrayList<GameCharacter> zero;
        ArrayList<GameCharacter> one;
        ArrayList<GameCharacter> two;
        zero = (ArrayList<GameCharacter>) square.getCharacters();

        one = (ArrayList<GameCharacter>) square.getExactlyOneMovementCharacters();

        two = (ArrayList<GameCharacter>) square.getExactlyTwoMovementsCharacters();

        e.add(zero);
        e.add(one);
        e.add(two);

        return e;
    }
}
