package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;

public class SeeNotOwnSquareCommand implements Command{

    public SeeNotOwnSquareCommand(){

    }


    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square){
        ArrayList<ArrayList<GameCharacter>> e = new ArrayList<ArrayList<GameCharacter>>();

        ArrayList<GameCharacter> e1 = (ArrayList<GameCharacter>) square.getVisibleCharacters();

        ArrayList<GameCharacter> e2 = (ArrayList<GameCharacter>)square.getCharacters();  //here I have the characters in my square
        //now I have to merge them


        for (GameCharacter t : (ArrayList<GameCharacter>) e1.clone()) {
            if(e2.contains(t)) {
                e1.remove(t);
            }
        }

        e.add(e1);
        return e;
    }

}
