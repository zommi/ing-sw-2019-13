package Cards;

import java.util.*;

/**
 * 
 */
public class NotOwnSquareCommand implements Command {

    public NotOwnSquareCommand() {
    }

    public ArrayList<ArrayList<Character>> execute(Square square);

    public Bullet prepareBullet();

}