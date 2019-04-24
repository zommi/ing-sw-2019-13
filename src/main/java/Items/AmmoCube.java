package Items;

import Constants.*;
import Cards.*;

/**
 * 
 */
public class AmmoCube extends Ownable implements AmmoTileItem {

    private Color color;
    private boolean valid;

    public AmmoCube(Color c) {
        this.color = c;
        this.valid = false;
    }


    /**
     * Reverses the value of valid.
     */
    public void toggleValid() {
        this.valid = !this.valid;
    }


    public boolean isValid() {
        return this.valid;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return color.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof AmmoCube)) {
            return false;
        }

        AmmoCube ammoCubeObject = (AmmoCube) obj;
        return this.color == ammoCubeObject.getColor() && this.valid == ammoCubeObject.isValid();
    }
}