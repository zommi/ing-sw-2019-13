package server.model.items;

import server.model.cards.Ownable;
import server.model.player.*;

/**
 * 
 */
public class FirstPlayerToken extends Ownable {

    /**
     * Constructor with one parameter
     */
    public FirstPlayerToken(ConcretePlayer p) {
        super(p);
    }
}