package server.model.cards;

import server.model.items.*;


public interface CardInterface extends ItemInterface {

    public void draw();

    public void getEffect();

    public String getName();

    public void discard();

}