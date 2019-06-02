package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import constants.Color;
import exceptions.InvalidMoveException;
import server.model.items.AmmoCube;

public class PowerUp {

    private int index;

    private String name;

    private Color value;

    private int numberOfCards;

    private String path;


    PowerUp(){
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PowerUp))
            return false;
        PowerUp powerUp2 = (PowerUp) obj;
        return name.equals(powerUp2.getName());
    }

    public String getPath() {
        return path;
    }
}