package server.model.cards;

import server.model.items.*;


public interface CardInterface extends ItemInterface {

    String getName();

    void discard();

}