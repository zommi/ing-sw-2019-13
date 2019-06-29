package view;

public class SetupConfirmAnswer implements ServerAnswer {

    private int skullNum;
    private int mapNum;
    private String characterName;
    private boolean spawn;
    private boolean respawn;


    public boolean isSpawn() {
        return spawn;
    }

    public void setSpawn(boolean spawn) {
        this.spawn = spawn;
    }

    public boolean isRespawn() {
        return respawn;
    }

    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

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
