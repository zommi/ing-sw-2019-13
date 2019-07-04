package client.info;

import java.io.Serializable;

public class SetupInfo implements Info, Serializable {
    private int mapChoice;

    private int initialSkulls;

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
