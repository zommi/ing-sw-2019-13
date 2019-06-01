package server.model.cards;

import java.io.Serializable;

/**
 * 
 */
public class Teleporter extends PowerUp implements Serializable {


    //This one does not affect the opponents, it only affects the character that is using it.
    public Teleporter() {
    }




    @Override
    public String getName() {
        return "Teleporter";
    }
}