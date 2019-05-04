package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;


public class AimOwnSquareCommand implements Command {


    public AimOwnSquareCommand() {
    }


    /**
     * Returns the characters on the same square of the player shooting
     * @param square where the player is
     * @return  an arraylist of arralist of the characters on the same square of the player shooting
     */
    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square){  //this method returns the arraylist of arraylist of characters that the player can shoot
        ArrayList<ArrayList<GameCharacter>> e = new ArrayList<ArrayList<GameCharacter>>();
        e.add((ArrayList)square.getCharacters());
        return e;
    }
}



