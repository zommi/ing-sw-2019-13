package Cards;

import Cards.Command;
import Player.Character;

import java.util.*;

/**
 * 
 */
public class RoomCanSeeCommand implements Command {


    public RoomCanSeeCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(Square square);


}