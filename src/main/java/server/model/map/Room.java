package server.model.map;

import constants.Color;
import server.model.player.GameCharacter;

import java.util.*;

/**
 * 
 */
public class Room {

    private Color color;
    private List<GameCharacter> charactersList;
    private List<SquareAbstract> squareList;

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

    public List<GameCharacter> getCharacters(){
        ArrayList<GameCharacter> returnedList = (ArrayList<GameCharacter>) charactersList;
        return (ArrayList<GameCharacter>)  returnedList.clone();
    }

    void addCharacter(GameCharacter gameCharacter){
        if(!charactersList.contains((gameCharacter)))
            charactersList.add(gameCharacter);
    }

    void removeCharacter(GameCharacter gameCharacter){
        charactersList.remove(gameCharacter);
    }

    void addSquare(SquareAbstract square){
        squareList.add(square);
    }


    public List<SquareAbstract> getSquares() {
        ArrayList<SquareAbstract> returnedList = (ArrayList<SquareAbstract>) squareList;
        return (ArrayList<SquareAbstract>) returnedList.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Room)) {
            return false;
        }

        Room room = (Room) obj;
        return this.color.equals(room.getColor());
    }
}