package server.model.items;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * object used to store the data read from a file JSON
 */
public class AmmoTileType {

    /**
     * number of red cubes in a tile
     */
    private int redCubes;

    /**
     * number of blue cubes in a tile
     */
    private int blueCubes;

    /**
     * number of yellow cubes in a tile
     */
    private int yellowCubes;

    /**
     * flag signaling if an ammo tile has a powerup card attached
     */
    @JsonProperty(value="powerup")
    private boolean powerup;

    /**
     * number of tiles of the same type
     */
    private int numberOfCards;

    private String path;

    AmmoTileType(){
        this.redCubes = 0;
        this.blueCubes = 0;
        this.yellowCubes = 0;
        this.powerup = false;
        this.numberOfCards = 0;
        this.path = "";
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

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return  "RedCubes: " + this.redCubes + '\n' +
                "BlueCubes: " + this.blueCubes + '\n' +
                "YellowCubes: " + this.yellowCubes + '\n' +
                "powerup:" + this.powerup + '\n';

    }
}
