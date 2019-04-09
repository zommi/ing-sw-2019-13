package Map;

import Constants.Color;
import Player.Character;

import java.util.*;

/**
 * 
 */
public class Room {

    private Color color;
    private List<Character> charactersList;

    private List<SquareAbstract> squareList;


    /**
     * Default constructor
     */
    public Room() {
    }

    public Color getColor() {
        return color;
    }

    public List<Character> getCharacter(){
        ArrayList<Character> returnedList = (ArrayList<Character>) charactersList;
        return (ArrayList<Character>)  returnedList.clone();
    }

    public void addCharacter(Character character){
        charactersList.add(character);
    }

    public void removeCharacter(Character character){
        charactersList.remove(character);
    }

    public void addSquare(SquareAbstract square){
        squareList.add(square);
    }



    /**
     * @return
     */
    public List<SquareAbstract> getSquares() {
        ArrayList<SquareAbstract> returnedList = (ArrayList<SquareAbstract>) squareList;
        return (ArrayList<SquareAbstract>) returnedList.clone();
    }

}