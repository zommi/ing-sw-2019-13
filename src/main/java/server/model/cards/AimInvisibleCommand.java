package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;

public class AimInvisibleCommand implements Command {


    public AimInvisibleCommand() {
    }


    /**
     * Returns the invisible characters from the position of the player shooting
     * @param square where the player is
     * @return  an arraylist of arralist of invisible characters from the position of the player shooting
     */
    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square){
        ArrayList<GameCharacter> e = (ArrayList<GameCharacter>) GameCharacter.getTakenCharacters();

        ArrayList<GameCharacter> e1 = (ArrayList)square.getVisibleCharacters();  //here I have the characters that I can see


        //now I have to make the intersection between them


        for (GameCharacter t : (ArrayList<GameCharacter>)e.clone()) {
            if(e1.contains(t)) {
                e.remove(t);
            }
        }


        ArrayList<ArrayList<GameCharacter>> result = new ArrayList<ArrayList<GameCharacter>>();
        result.add(e);

        return result;
    }

}