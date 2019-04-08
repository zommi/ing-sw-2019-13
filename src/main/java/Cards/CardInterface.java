package Cards;

import java.util.*;
import Items.*;


public interface CardInterface extends ItemInterface {


    public void play();

    public void draw();

    public void getEffect();

    public void getName();

    public void discard();

}