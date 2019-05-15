package client;

import server.ServerAnswer.ServerAnswer;

public interface ReceiverInterface {
    public abstract void publishMessage(ServerAnswer answer);

}
