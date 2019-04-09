package Map;

import Constants.Color;
import Player.Character;

import java.util.*;

/**
 * 
 */
public class Room {

    private Color color;

    private ArrayList<SquareAbstract> squareList;


    /**
     * Default constructor
     */
    public Room() {
    }

    public Color getColor() {
        return color;
    }

    public void addSquare(SquareAbstract square){
        squareList.add(square);
    }

    public List<Character> getCharacters() {
        // TODO implement here
        return new ArrayList<Character>();
    }

    /**
     * @return
     */
    public List<SquareAbstract> getSquares() {
        return (ArrayList<SquareAbstract>) squareList.clone();
    }

}