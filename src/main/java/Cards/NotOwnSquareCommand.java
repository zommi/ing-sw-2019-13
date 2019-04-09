package Cards;

import java.util.*;

import Player.Character;

public class NotOwnSquareCommand implements Command {

    public NotOwnSquareCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(Map.NoSpawnSquare square){
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