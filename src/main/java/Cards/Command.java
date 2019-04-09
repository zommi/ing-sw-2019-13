package Cards;

import java.util.*;
import Map.*;
import Player.Character;


public interface Command {

    public ArrayList<ArrayList<Character>> execute(Square square);

}