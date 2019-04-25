package Cards;
import Constants.Directions;
import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;


public class FlameThrowerCommand implements Command{

    private Constants.Directions direction;

    public FlameThrowerCommand() {
    }

    public void setdirection(Constants.Directions c) //the controller will ask the user to choose between n,s,e,w
    {
        this.direction = c;
    }

    /**
     * Returns the characters that the player shooting can see 1 move away from him and possibly 1 more move away in the same direction.
     * @param square where the player is
     * @return an arraylist of arralist of the characters that the player shooting can see 1 move away from him and possibly 1 more move away in the same direction.
     */
    public ArrayList<ArrayList<Character>> execute(SquareAbstract square) {

        ArrayList<SquareAbstract> eN = (ArrayList<SquareAbstract>)square.getTwoSquaresInTheSameDirection(Directions.NORTH);
        ArrayList<SquareAbstract> eS = (ArrayList<SquareAbstract>)square.getTwoSquaresInTheSameDirection(Directions.SOUTH);
        ArrayList<SquareAbstract> eW = (ArrayList<SquareAbstract>)square.getTwoSquaresInTheSameDirection(Directions.WEST);
        ArrayList<SquareAbstract> eE = (ArrayList<SquareAbstract>)square.getTwoSquaresInTheSameDirection(Directions.EAST);

        ArrayList<ArrayList<Character>> cN = new ArrayList<ArrayList<Character>>();
        ArrayList<ArrayList<Character>> cS = new ArrayList<ArrayList<Character>>();
        ArrayList<ArrayList<Character>> cW = new ArrayList<ArrayList<Character>>();
        ArrayList<ArrayList<Character>> cE = new ArrayList<ArrayList<Character>>();


        for (SquareAbstract t : eN) {
            cN.add((ArrayList<Character>)t.getCharacters());
        }

        for (SquareAbstract t : eS) {
            cS.add((ArrayList<Character>)t.getCharacters());
        }

        for (SquareAbstract t : eW) {
            cW.add((ArrayList<Character>)t.getCharacters());
        }

        for (SquareAbstract t : eE) {
            cE.add((ArrayList<Character>)t.getCharacters());
        }

        if(this.direction.getAbbreviation() == "n")
            return (ArrayList<ArrayList<Character>>)cN.clone();
        else if(this.direction.getAbbreviation() == "s")
            return (ArrayList<ArrayList<Character>>)cS.clone();
        else if(this.direction.getAbbreviation() == "e")
            return (ArrayList<ArrayList<Character>>)cE.clone();
        else
            return (ArrayList<ArrayList<Character>>)cW.clone();

    }

}
