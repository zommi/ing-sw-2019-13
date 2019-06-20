package view;

public class SetupConfirmAnswer implements ServerAnswer {

    private int skullNum;
    private int mapNum;
    private String characterName;

    public String getCharacterName() {
        return characterName;
    }

    public int getSkullNum() {
        return skullNum;
    }

    public int getMapNum() {
        return mapNum;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public void setSkullNum(int skullNum) {
        this.skullNum = skullNum;
    }

    public void setMapNum(int mapNum) {
        this.mapNum = mapNum;
    }
}
