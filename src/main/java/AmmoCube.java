
import java.util.*;

/**
 * 
 */
public class AmmoCube extends Ownable {

    private Color color;
    private boolean valid;

    public AmmoCube(Color c) {
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

    public Color getColor() {
        return this.color;
    }

}