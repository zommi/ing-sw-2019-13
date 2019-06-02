package server.model.cards;

import java.io.Serializable;

/**
 * 
 */
public class TagbackGrenade extends PowerUp implements Serializable {

    //This PowerUp can be used during another's turn. So we can check the value of bullet.getDamage() everytime a player receive a bullet
    //if it is > 0 then this can be used.

    public TagbackGrenade() {
    }

    @Override
    public String getName() {
        return "Tagback Grenade";
    }
}