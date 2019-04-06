
import java.util.*;

/**
 * 
 */
public enum Character {
    DESTRUCTOR (0),
    BANSHEE (1),
    DOZER (2),
    VIOLET (3),
    SPROG (4);

    private static final int NUM_FIGURES = 5;
    private static boolean[] figureChosen = new boolean[NUM_FIGURES];
    private int id;

    private Square position;

    Character(int id){
        if(!isTaken(id)) {
            this.id = id;
            setTaken(id);
        }
    }

    public static void setTaken(int id){
        figureChosen[id] = true;
    }

    public static boolean isTaken(int id){
        return figureChosen[id];
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