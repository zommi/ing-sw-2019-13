package Cards;

import java.util.*;
import Items.*;


public interface CardInterface extends ItemInterface {


    public Bullet play();

    public void draw();

    public void getEffect();

    public String getName();

    public void discard();

}