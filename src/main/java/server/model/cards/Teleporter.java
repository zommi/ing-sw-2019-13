package server.model.cards;

import java.io.Serializable;
import java.util.Scanner;

/**
 * 
 */
public class Teleporter extends Powerup implements Serializable {


    //This one does not affect the opponents, it only affects the character that is using it.
    public Teleporter() {
    }




    @Override
    public String getName() {
        return "Teleporter";
    }
}