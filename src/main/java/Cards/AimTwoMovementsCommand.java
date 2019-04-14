package Cards;

import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;


public class AimTwoMovementsCommand implements Command {

    public AimTwoMovementsCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        e.add((ArrayList<Character>)square.getUpToOneMovementCharacters());

        return e;
    }


}