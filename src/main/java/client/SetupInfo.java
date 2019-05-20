package client;

import java.io.Serializable;

public class SetupInfo implements Info, Serializable {
    private int mapChoice;

    private int initialSkulls;

    private String playerName;

    private String characterChosen;

    private boolean characterSetup;

    public SetupInfo(int mapChoice, int initialSkulls,
                     String playerName) {
        this.mapChoice = mapChoice;
        this.initialSkulls = initialSkulls;
        this.playerName = playerName;
    }

    public boolean isCharacterSetup() {
        return characterSetup;
    }

    public SetupInfo(String characterChosen) {
        this.characterChosen = characterChosen;
        this.characterSetup = true;
    }

    public int getMapChoice() {
        return mapChoice;
    }

    public int getInitialSkulls() {
        return initialSkulls;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCharacterChosen() {
        return characterChosen;
    }


}
