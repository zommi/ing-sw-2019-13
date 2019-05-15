package server;

import server.ServerAnswer.ServerAnswer;

public abstract class Publisher {
        public abstract void sendMessage(ServerAnswer message);
}

