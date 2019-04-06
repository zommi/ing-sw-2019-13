
import java.util.*;

/**
 * 
 */
public class Character {
    Figure figure;
    Square position;

    /**
     * Default constructor
     */
    public Character() {
    }


    private void setPosition(Square position){
        this.position = position;
    }
    /**
     *
     * @param sq
     */
    public void move(Square sq) {
        /*
            if(position.distance(sq) <= 3){
            setPosition(sq);
        }
         */
    }

    /**
     * @return
     */
    public Square getPosition() {
        return position;
    }

    public void spawn(SpawnPoint sp) {
        if(position == null){
            setPosition(sp.getSquare());
        }
    }
}