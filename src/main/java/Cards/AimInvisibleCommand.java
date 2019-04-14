package Cards;

import Map.Square;
import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;

public class AimInvisibleCommand implements Command {


    public AimInvisibleCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){
        ArrayList<Character> e = (ArrayList<Character>)Character.getTakenCharacters();

        ArrayList<Character> e1 = (ArrayList)square.getVisibleCharacters();  //here I have the characters that I can see


        //now I have to make the intersection between them


        for (Character t : e) {
            if(e1.contains(t)) {
                e.remove(t);
            }
        }

        ArrayList<ArrayList<Character>> result = new ArrayList<ArrayList<Character>>();
        result.add(e);

        return result;
    }

}