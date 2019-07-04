package server.model.cards;

import java.io.Serializable;

public class Newton extends PowerUp implements Serializable {

    public Newton() {
    }


    @Override
    public String getName() {
        return "Newton";
    }
}