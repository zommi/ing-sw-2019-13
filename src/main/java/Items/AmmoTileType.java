package Items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmmoTileType {
    private int redCubes;

    private int blueCubes;

    private int yellowCubes;

    @JsonProperty(value="powerup")
    private boolean powerup;

    private int numberOfCards;

    AmmoTileType(){
        this.redCubes = 0;
        this.blueCubes = 0;
        this.yellowCubes = 0;
        this.powerup = false;
        this.numberOfCards = 0;
    }

    public int getBlueCubes() {
        return blueCubes;
    }

    public int getRedCubes() {
        return redCubes;
    }

    public int getYellowCubes() {
        return yellowCubes;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public boolean hasPowerup() {
        return powerup;
    }

    @Override
    public String toString() {
        return  "RedCubes: " + this.redCubes + '\n' +
                "BlueCubes: " + this.blueCubes + '\n' +
                "YellowCubes: " + this.yellowCubes + '\n' +
                "powerup:" + this.powerup + '\n';

    }
}
