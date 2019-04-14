package Cards;
import Constants.Directions;
import Map.Square;
import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;


public class FlameThrowerCommand {

    private char direction;

    public FlameThrowerCommand() {
    }

    public void setdirection(char c) //the controller will ask the user to choose between n,s,e,w
    {
        this.direction = c;
    }

    public ArrayList<ArrayList<Character>> execute(Square square) {

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

        if(this.direction == 'n')
            return (ArrayList<ArrayList<Character>>)cN.clone();
        else if(this.direction == 's')
            return (ArrayList<ArrayList<Character>>)cS.clone();
        else if(this.direction == 'e')
            return (ArrayList<ArrayList<Character>>)cE.clone();
        else                                                            //if(this.direction == 'w')
            return (ArrayList<ArrayList<Character>>)cW.clone();

        //now the user has to say to me which direction he wants. Otherwise I don't know what to return.

    }

}
