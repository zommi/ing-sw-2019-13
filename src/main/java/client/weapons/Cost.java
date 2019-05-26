package client.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Cost sum(Cost cost){
        int sumRed;
        int sumBlue;
        int sumYellow;
        sumRed = this.red + cost.red;
        sumBlue = this.blue + cost.blue;
        sumYellow = this.yellow + cost.yellow;
        return new Cost(sumRed, sumBlue, sumYellow);

    }

    @Override
    public boolean equals(Object object){
        if (object == null) {
            return false;
        }

        if (!(object instanceof Cost)) {
            return false;
        }

        Cost costObject = (Cost) object;

        return (this.red == costObject.red && this.blue == costObject.blue && this.yellow == costObject.yellow);
    }

    public String toString(){
        //TODO
        return "";
    }
}
