package Cards;

import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;

public class NotOwnSquareCommand implements Command {

    public NotOwnSquareCommand() {
    }

    /**
     * Returns the characters that are not in the same square of the player shooting
     * @param square where the player is
     * @return an arraylist of arralist of the characters that are not in the same square of the player shooting
     */
    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){
        //The not own are all the characters without the ones in my own square.
        //getTakenCharacters da Character
        ArrayList<Character> e = (ArrayList<Character>)Character.getTakenCharacters(); //here I have all the characters
        ArrayList<Character> e1 = (ArrayList)square.getCharacters();  //here I have the characters in my square
        //now I have to merge them


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