package server.model.cards;

import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.Character;

import java.util.ArrayList;


public class RoomCanSeeCommand implements Command {


    public RoomCanSeeCommand() {
    }

    /**
     * Returns the characters that are in a room that the player shooting can see
     * @param square where the player is
     * @return an arraylist of arralist of the characters that are in a room that the player shooting can see
     */
    public ArrayList<ArrayList<Character>> execute(SquareAbstract square) {
        ArrayList<Room> e = (ArrayList<Room>)square.getVisibleRooms();
        ArrayList<ArrayList<Character>> e1 = new ArrayList<ArrayList<Character>>();

        for (Room t : e) {
                e1.add((ArrayList<Character>)t.getCharacters());
            }


        return e1;
    }


}