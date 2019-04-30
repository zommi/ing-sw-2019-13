package server.model.items;

import server.model.cards.Ownable;
import server.model.player.*;

/**
 * 
 */
public class DamageToken extends Ownable {

    /**
     * Constructor with one parameter
     */
    public DamageToken(ConcretePlayer p) {
        super(p);
    }


}