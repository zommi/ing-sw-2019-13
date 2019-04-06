
import java.util.*;

/**
 * 
 */
public interface CardInterface extends ItemInterface {

    /**
     * @return
     */
    public void play();

    /**
     * @return
     */
    public void draw();

    /**
     * @return
     */
    public void getEffect();

    /**
     * @return
     */
    public void getName();

    /**
     * @return
     */
    public void discard();

}