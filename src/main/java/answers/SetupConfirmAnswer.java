package answers;

public class SetupConfirmAnswer implements ServerAnswer {

    private boolean spawn;
    private boolean respawn;
    private boolean serverOffline;


    public boolean isServerOffline() {
        return serverOffline;
    }

    public void setServerOffline(boolean serverOffline) {
        this.serverOffline = serverOffline;
    }

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
}
