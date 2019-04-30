package server.model.cards;

import server.model.items.*;


public interface CardInterface extends ItemInterface {


    //public Bullet play(int extra);

    public void draw();

    public void getEffect();

    public String getName();

    public void discard();

}