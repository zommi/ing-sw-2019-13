package client.info;

public class ReconnectInfo implements Info {

    private int clientId;

    public ReconnectInfo(int clientId){
        this.clientId = clientId;
    }

    public int getClientId() {
        return clientId;
    }
}
