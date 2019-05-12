package server.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.Color;
import server.model.items.AmmoCube;

import java.util.ArrayList;
import java.util.List;

public class Cost {

    @JsonProperty
    private int red;

    @JsonProperty
    private int blue;

    @JsonProperty
    private int yellow;

    @JsonCreator
    public Cost(
            @JsonProperty("red") int red,
            @JsonProperty("blue") int blue,
            @JsonProperty("yellow") int yellow) {
        this.red = red;
        this.blue = blue;
        this.yellow = yellow;
    }
}
