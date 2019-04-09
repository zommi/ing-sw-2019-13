package Player;

import java.util.*;
import Map.*;

/**
 * 
 */
public abstract class PlayerAbstract {

    /**
     * @return
     */
    PlayerAbstract(){
    }


    public Figure chooseFigure(Figure selection) {
        System.out.println("You chose: " + selection +". Great Choice!");
        Character.setTaken(selection);
        return selection;
    }

    /**
     * @return
     */
    public abstract void spawn(SquareAbstract sp);

    /**
     * @return
     */
    public void move() {
    }

    /**
     * @return
     */
    public void shoot() {
    }

    /**
     * @return
     */
    public void collect() {
    }

    /**
     * @return
     */
    public PlayerState getPlayerState() {
        return null;
    }

}