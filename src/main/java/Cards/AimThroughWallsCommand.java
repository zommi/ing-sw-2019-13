package Cards;

import java.util.*;
import Map.*;
import Player.Character;

/**
 * 
 */
public class AimThroughWallsCommand implements Command {

    public AimThroughWallsCommand() {
    }

<<<<<<< HEAD
    public ArrayList<ArrayList<Character>> execute(Square square){
        ArrayList<Character> e = (ArrayList)square.getCharactersThroughWalls();

        ArrayList<ArrayList<Character>> result = new ArrayList<ArrayList<Character>>(); //this will be the return for my player

        ArrayList<Character> eN = new ArrayList<Character>(); //NORTH
        ArrayList<Character> eS = new ArrayList<Character>(); //SOUTH
        ArrayList<Character> eW = new ArrayList<Character>(); //WEST
        ArrayList<Character> eE = new ArrayList<Character>(); //EAST
=======

    public ArrayList<ArrayList<Character>> execute(Square square){return null;}
>>>>>>> 44bd45b03f93d9f0f931d24e52ae979ab25ff80b

        //now I have to create an array of array with 4 different lists.
        for (Character t : e) {
            if (square.getxValue() == t.getPosition().getxValue()) {
                if (square.getyValue() > t.getPosition().getyValue()) { //if this is true then we are considering the SOUTH LIST
                    eS.add(t);
                } else if (square.getyValue() < t.getPosition().getyValue()) { //if this is true then we are considering the NORTH LIST
                    eN.add(t);
                }
            }
            else if(square.getyValue() == t.getPosition().getyValue()){
                 if(square.getxValue() < t.getPosition().getxValue()) { //if this is true then we are considering the EAST LIST
                     eE.add(t);
                 }
                 else if(square.getxValue() > t.getPosition().getxValue()){ //if this is true then we are considering the WEST LIST
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