package Cards;

import Cards.Command;

import java.util.*;
import Map.*;
import Player.Character;


public class RoomCanSeeCommand implements Command {


    public RoomCanSeeCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(Square square) {
        ArrayList<Room> e = (ArrayList<Room>)square.getVisibleRooms();
        ArrayList<ArrayList<Character>> e1 = new ArrayList<ArrayList<Character>>();

        for (Room t : e) {
                e1.add((ArrayList<Character>)t.getCharacters());
            }


        return e1;
    }


}