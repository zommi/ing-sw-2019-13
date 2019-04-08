
import java.util.*;

/**
 * 
 */
public class AmmoCube extends Ownable {

    private int color;  //Have a look at the costants class.
    private boolean valid;

    public AmmoCube(int c) {
        this.color = c;
    }


    /**
     * Reverses the value of valid.
     */
    public void toggleValid() {
        if(this.valid == true)
            this.valid = false;
        else
            this.valid = true;
    }


    public boolean isValid() {
        return this.valid;
    }

    public int getColor() {
        return this.color;
    }

}