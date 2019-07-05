package client.info;

/**
 * Info: used to connect the client to the server after he has been disconnected
 */
public class ReconnectInfo implements Info {

    /**
     * Client ID of the player to reconnect
     */
    private int clientId;

    public ReconnectInfo(int clientId){
        this.clientId = clientId;
    }

    public int getClientId() {
        return clientId;
    }
}
