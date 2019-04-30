package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.Character;

import java.util.ArrayList;

public class AimInvisibleCommand implements Command {


    public AimInvisibleCommand() {
    }


    /**
     * Returns the invisible characters from the position of the player shooting
     * @param square where the player is
     * @return  an arraylist of arralist of invisible characters from the position of the player shooting
     */
    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){
        ArrayList<Character> e = (ArrayList<Character>)Character.getTakenCharacters();

        ArrayList<Character> e1 = (ArrayList)square.getVisibleCharacters();  //here I have the characters that I can see


        //now I have to make the intersection between them


        for (Character t : (ArrayList<Character>)e.clone()) {
            if(e1.contains(t)) {
                e.remove(t);
            }
        }


        ArrayList<ArrayList<Character>> result = new ArrayList<ArrayList<Character>>();
        result.add(e);

        return result;
    }

}