package server.model.cards;

import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;

/**
 * 
 */
public class AimThroughWallsCommand implements Command {

    public AimThroughWallsCommand() {
    }

    /**
     * Returns the characters that the player shooting can see through walls depending on the direction
     * @param square where the player is
     * @return an arraylist of arralist of the characters that the player shooting can see through walls depending on the direction
     */
    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square){
        ArrayList<GameCharacter> e = (ArrayList)square.getCharactersThroughWalls();

        ArrayList<ArrayList<GameCharacter>> result = new ArrayList<ArrayList<GameCharacter>>(); //this will be the return for my player

        ArrayList<GameCharacter> eN = new ArrayList<GameCharacter>(); //NORTH
        ArrayList<GameCharacter> eS = new ArrayList<GameCharacter>(); //SOUTH
        ArrayList<GameCharacter> eW = new ArrayList<GameCharacter>(); //WEST
        ArrayList<GameCharacter> eE = new ArrayList<GameCharacter>(); //EAST

        //now I have to create an array of array with 4 different lists.
        for (GameCharacter t : e) {
            if (square.getRow() == t.getPosition().getRow()) {
                if (square.getCol() > t.getPosition().getCol()) { //if this is true then we are considering the SOUTH LIST
                    eS.add(t);
                } else if (square.getCol() < t.getPosition().getCol()) {//if this is true then we are considering the NORTH LIST
                    eN.add(t);
                } else if(square.getCol() == t.getPosition().getCol()){
                    eN.add(t);
                    eS.add(t);
                    eW.add(t);
                    eE.add(t);
                }

            }
            else if(square.getCol() == t.getPosition().getCol()){
                 if(square.getRow() < t.getPosition().getRow()) { //if this is true then we are considering the EAST LIST
                     eE.add(t);
                 }
                 else if(square.getRow() > t.getPosition().getRow()){ //if this is true then we are considering the WEST LIST
                     eW.add(t);
                  }
            }
        }

        result.add(eN);
        result.add(eS);
        result.add(eW);
        result.add(eE);

        return result;
    }
}