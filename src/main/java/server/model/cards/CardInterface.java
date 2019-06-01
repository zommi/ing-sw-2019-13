package server.model.cards;

import server.model.items.*;


public interface CardInterface extends ItemInterface {

    void draw();

    String getName();

    void discard();

}