package server.model.cards;

import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;

import java.util.ArrayList;


public class RoomCanSeeCommand implements Command {


    public RoomCanSeeCommand() {
    }

    /**
     * Returns the characters that are in a room that the player shooting can see
     * @param square where the player is
     * @return an arraylist of arralist of the characters that are in a room that the player shooting can see
     */
    public ArrayList<ArrayList<GameCharacter>> execute(SquareAbstract square) {
        ArrayList<Room> e = (ArrayList<Room>)square.getVisibleRooms();
        ArrayList<ArrayList<GameCharacter>> e1 = new ArrayList<ArrayList<GameCharacter>>();

        for (Room t : e) {
                e1.add((ArrayList<GameCharacter>)t.getCharacters());
            }


        return e1;
    }


}