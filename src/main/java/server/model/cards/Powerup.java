package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import constants.Color;
import exceptions.InvalidMoveException;
import server.model.items.AmmoCube;

public class Powerup {

    private int index;

    private String name;

    private Color value;

    private int numberOfCards;


    Powerup(){
        this.index = -1;
        this.name = "UNDEFINED";
        this.value = Color.UNDEFINED;
        this.numberOfCards = 0;
    }

    public String getName(){
        return this.name;
    }

    public int getNumberOfCards() {
        return this.numberOfCards;
    }

    public Color getValue() {
        return this.value;
    }

    public int getIndex() {
        return this.index;
    }
}