package server.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;
import constants.Color;
import exceptions.InvalidMoveException;
import server.model.items.AmmoCube;

public class PowerUp {

    @JsonProperty
    private int index;

    @JsonProperty
    private String name;

    @JsonProperty
    private Color value;

    @JsonProperty
    private int numberOfCards;

    @JsonProperty
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