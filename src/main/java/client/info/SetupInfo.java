package client.info;

import java.io.Serializable;

/**
 * Info: used to setup a player at the start of the match
 */
public class SetupInfo implements Info, Serializable {
    /**
     * Map chosen by the player
     */
    private int mapChoice;
    /**
     * Number of initial skulls chosen by the player
     */
    private int initialSkulls;
    /**
     * The name of the character chosen by the player
     */
    private String characterName;


    public int getMapChoice() {
        return mapChoice;
    }

    public int getInitialSkulls() {
        return initialSkulls;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setMapChoice(int mapChoice) {
        this.mapChoice = mapChoice;
    }

    public void setInitialSkulls(int initialSkulls) {
        this.initialSkulls = initialSkulls;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
}
