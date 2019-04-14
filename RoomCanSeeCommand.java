package Cards;

import Map.Room;
import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;


public class RoomCanSeeCommand implements Command {


    public RoomCanSeeCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(SquareAbstract square) {
        ArrayList<Room> e = (ArrayList<Room>)square.getVisibleRooms();
        ArrayList<ArrayList<Character>> e1 = new ArrayList<ArrayList<Character>>();

        for (Room t : e) {
                e1.add((ArrayList<Character>)t.getCharacters());
            }


        return e1;
    }


}