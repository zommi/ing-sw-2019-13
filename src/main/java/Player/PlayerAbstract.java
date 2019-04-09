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


    public Figure chooseFigure() {
        int selection;
        Scanner sc = new Scanner(System.in);
        //save a list with all the available characters
        List<Figure> list = Character.getValidFigures();
        //Read input
        do {
            System.out.println("Choose your character:" + list.toString());
            selection = sc.nextInt();
        } while(!list.contains(Figure.getValue(selection)));
        System.out.println("You chose: " + Figure.getValue(selection) +". Great Choice!");
        //set True corresponding cell
        Character.setTaken(Figure.getValue(selection));
        return Figure.getValue(selection);
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