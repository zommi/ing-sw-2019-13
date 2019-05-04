package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;

public class NotOwnSquareCommand implements Command {

    public NotOwnSquareCommand() {
    }

    /**
     * Returns the characters that are not in the same square of the player shooting
     * @param square where the player is
     * @return an arraylist of arralist of the characters that are not in the same square of the player shooting
     */
    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square){
        //The not own are all the characters without the ones in my own square.
        //getTakenCharacters da GameCharacter
        ArrayList<GameCharacter> e = (ArrayList<GameCharacter>) GameCharacter.getTakenCharacters(); //here I have all the characters
        ArrayList<GameCharacter> e1 = (ArrayList)square.getCharacters();  //here I have the characters in my square
        //now I have to merge them


        for (GameCharacter t : e) {
            if(e1.contains(t)) {
                e.remove(t);
            }
        }

        ArrayList<ArrayList<GameCharacter>> result = new ArrayList<ArrayList<GameCharacter>>();
        result.add(e);

        return result;
    }


}