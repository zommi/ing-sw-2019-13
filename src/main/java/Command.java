
import java.util.*;


public interface Command {

    public ArrayList<ArrayList<Character>> execute();

    public Bullet prepareBullet();
}