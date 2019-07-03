package client.weapons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import constants.Color;
import constants.Constants;
import server.model.cards.PowerUpCard;

import java.io.Serializable;
import java.util.List;

public class Cost implements Serializable {

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

    public Cost(Cost cost){
        this.red = cost.red;
        this.blue = cost.blue;
        this.yellow = cost.yellow;
    }

    public static Cost getCost(Color color){

        int tblue = 0;
        int tred = 0;
        int tyellow = 0;

        switch(color){
            case RED: tred++; break;
            case BLUE: tblue++; break;
            case YELLOW: tyellow++; break;
            default: //zero cost
        }

        return new Cost(tred, tblue, tyellow);
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

    public Cost subtract(Cost cost){
        int resultRed;
        int resultBlue;
        int resultYellow;

        if(this.red > cost.red)
            resultRed = this.red - cost.red;
        else resultRed = 0;

        if(this.yellow > cost.yellow)
            resultYellow = this.yellow - cost.yellow;
        else resultYellow = 0;

        if(this.blue > cost.blue)
            resultBlue = this.blue - cost.blue;
        else resultBlue = 0;

        return new Cost(resultRed, resultBlue, resultYellow);
    }

    public static Cost powerUpListToCost(List<PowerUpCard> powerUpCards){
        int resultRed = 0;
        int resultYellow = 0;
        int resultBlue = 0;

        for(PowerUpCard powerUpCard : powerUpCards){
            if(powerUpCard.getColor() == Color.RED)
                resultRed++;
            else if(powerUpCard.getColor() == Color.BLUE)
                resultBlue++;
            else if(powerUpCard.getColor() == Color.YELLOW)
                resultYellow++;
        }

        return new Cost(resultRed, resultBlue, resultYellow);
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
        return red + " red, " + blue + " blue, " + yellow + " yellow";

    }

    public String printOnCli(){
        return Color.RED.getAnsi() + Constants.AMMOCUBE_CLI_ICON + " x" + red + "    " +
                Color.BLUE.getAnsi() + Constants.AMMOCUBE_CLI_ICON + " x" + blue + "    " +
                Color.YELLOW.getAnsi() + Constants.AMMOCUBE_CLI_ICON + " x" + yellow + Constants.ANSI_RESET;
    }

    public int getBlue() {
        return blue;
    }

    public int getRed() {
        return red;
    }

    public int getYellow() {
        return yellow;
    }
}
