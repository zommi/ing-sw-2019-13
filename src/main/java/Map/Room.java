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
    private List<Square> squareList;

    /**
     * Default constructor
     */
    public Room(Color color) {
        this.color = color;
        charactersList = new ArrayList<>();
        squareList = new ArrayList<>();
    }

    public Color getColor() {
        return color;
    }

    public List<Character> getCharacters(){
        ArrayList<Character> returnedList = (ArrayList<Character>) charactersList;
        return (ArrayList<Character>)  returnedList.clone();
    }

    public void addCharacter(Character character){
        if(!charactersList.contains((character)))
            charactersList.add(character);
    }

    public void removeCharacter(Character character){
        charactersList.remove(character);
    }

    public void addSquare(Square square){
        squareList.add(square);
    }


    public List<Square> getSquares() {
        ArrayList<Square> returnedList = (ArrayList<Square>) squareList;
        return (ArrayList<Square>) returnedList.clone();
    }

}