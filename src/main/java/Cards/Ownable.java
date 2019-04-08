package Cards;

import java.util.*;
import Items.*;
import Player.*;

/**
 * 
 */
public abstract class Ownable implements ItemInterface {

    private ConcretePlayer owner;

    /**
     * Contructor with 1 parameter
     */

    public Ownable(){

    }
    public Ownable(ConcretePlayer p) {
        owner = p;
    }

    public ConcretePlayer getOwner() {
        return this.owner;
    }

}