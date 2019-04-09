package Cards;

import java.util.*;
import Map.*;


public interface Command {

    public ArrayList<ArrayList<Character>> execute(Square square);

}