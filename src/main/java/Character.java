
import java.util.*;

/**
 * 
 */
public class Character {
    private Figure figure;
    private Square position;

    /**
     * Default constructor
     */
    public Character(Figure f) {
        this.figure = f;
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
        } else{
            System.out.println("Invalid move");
        }
    }
}