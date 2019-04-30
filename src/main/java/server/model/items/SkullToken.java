package server.model.items;

import server.model.cards.Ownable;
import server.model.player.*;

/**
 * 
 */
public class SkullToken extends Ownable {

    /**
     * Constructor with one parameter
     */
    public SkullToken(ConcretePlayer p) {
        super(p);
    }


}