package Cards;
import java.util.*;

import Player.Character;


public class FlameThrowerCommand {

    private char direction;

    public FlameThrowerCommand() {
    }

    public void setdirection(char c) //i need this method in order to communicate the direction from the controller to the model
    {
        this.direction = c;
    }

    public ArrayList<ArrayList<Character>> execute(Map.NoSpawnSquare square) {

        ArrayList<Map.Square> eN = (ArrayList<Map.Square>)square.getTwoSquaresInTheSameDirection('n');
        ArrayList<Map.Square> eS = (ArrayList<Map.Square>)square.getTwoSquaresInTheSameDirection('s');
        ArrayList<Map.Square> eW = (ArrayList<Map.Square>)square.getTwoSquaresInTheSameDirection('w');
        ArrayList<Map.Square> eE = (ArrayList<Map.Square>)square.getTwoSquaresInTheSameDirection('e');

        ArrayList<ArrayList<Character>> cN = new ArrayList<ArrayList<Character>>();
        ArrayList<ArrayList<Character>> cS = new ArrayList<ArrayList<Character>>();
        ArrayList<ArrayList<Character>> cW = new ArrayList<ArrayList<Character>>();
        ArrayList<ArrayList<Character>> cE = new ArrayList<ArrayList<Character>>();


        for (Map.Square t : eN) {
            cN.add((ArrayList<Character>)t.getCharacters());
        }

        for (Map.Square t : eS) {
            cS.add((ArrayList<Character>)t.getCharacters());
        }

        for (Map.Square t : eW) {
            cW.add((ArrayList<Character>)t.getCharacters());
        }

        for (Map.Square t : eE) {
            cE.add((ArrayList<Character>)t.getCharacters());
        }

        if(this.direction == 'n')
            return (ArrayList<ArrayList<Character>>)cN.clone();
        else if(this.direction == 's')
            return (ArrayList<ArrayList<Character>>)cS.clone();
        else if(this.direction == 'e')
            return (ArrayList<ArrayList<Character>>)cE.clone();
        else //TODO sistema sta roba perchè ci vuole un return incondizionato if(this.direction == 'w')
            return (ArrayList<ArrayList<Character>>)cW.clone();

        //now the user has to say to me which direction he wants. Otherwise I don't know what to return.

    }

}
