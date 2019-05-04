package server.model.cards;
import constants.Directions;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;


public class FlameThrowerCommand implements Command{

    private constants.Directions direction;

    public FlameThrowerCommand() {
    }

    public void setdirection(constants.Directions c) //the controller will ask the user to choose between n,s,e,w
    {
        this.direction = c;
    }

    /**
     * Returns the characters that the player shooting can see 1 move away from him and possibly 1 more move away in the same direction.
     * @param square where the player is
     * @return an arraylist of arralist of the characters that the player shooting can see 1 move away from him and possibly 1 more move away in the same direction.
     */
    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square) {

        ArrayList<SquareAbstract> eN = (ArrayList<SquareAbstract>)square.getTwoSquaresInTheSameDirection(Directions.NORTH);
        ArrayList<SquareAbstract> eS = (ArrayList<SquareAbstract>)square.getTwoSquaresInTheSameDirection(Directions.SOUTH);
        ArrayList<SquareAbstract> eW = (ArrayList<SquareAbstract>)square.getTwoSquaresInTheSameDirection(Directions.WEST);
        ArrayList<SquareAbstract> eE = (ArrayList<SquareAbstract>)square.getTwoSquaresInTheSameDirection(Directions.EAST);

        ArrayList<ArrayList<GameCharacter>> cN = new ArrayList<ArrayList<GameCharacter>>();
        ArrayList<ArrayList<GameCharacter>> cS = new ArrayList<ArrayList<GameCharacter>>();
        ArrayList<ArrayList<GameCharacter>> cW = new ArrayList<ArrayList<GameCharacter>>();
        ArrayList<ArrayList<GameCharacter>> cE = new ArrayList<ArrayList<GameCharacter>>();


        for (SquareAbstract t : eN) {
            cN.add((ArrayList<GameCharacter>)t.getCharacters());
        }

        for (SquareAbstract t : eS) {
            cS.add((ArrayList<GameCharacter>)t.getCharacters());
        }

        for (SquareAbstract t : eW) {
            cW.add((ArrayList<GameCharacter>)t.getCharacters());
        }

        for (SquareAbstract t : eE) {
            cE.add((ArrayList<GameCharacter>)t.getCharacters());
        }

        if(this.direction.getAbbreviation() == "n")
            return (ArrayList<ArrayList<GameCharacter>>)cN.clone();
        else if(this.direction.getAbbreviation() == "s")
            return (ArrayList<ArrayList<GameCharacter>>)cS.clone();
        else if(this.direction.getAbbreviation() == "e")
            return (ArrayList<ArrayList<GameCharacter>>)cE.clone();
        else
            return (ArrayList<ArrayList<GameCharacter>>)cW.clone();

    }

}
