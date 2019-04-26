package Model.Cards;

import Model.Map.SquareAbstract;
import Model.Player.Character;

import java.util.ArrayList;

public class SeeNotOwnSquareCommand implements Command{

    public SeeNotOwnSquareCommand(){

    }


    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();

        ArrayList<Character> e1 = (ArrayList<Character>) square.getVisibleCharacters();

        ArrayList<Character> e2 = (ArrayList<Character>)square.getCharacters();  //here I have the characters in my square
        //now I have to merge them


        for (Character t : (ArrayList<Character>) e1.clone()) {
            if(e2.contains(t)) {
                e1.remove(t);
            }
        }

        e.add(e1);
        return e;
    }

}
