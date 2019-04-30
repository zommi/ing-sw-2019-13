package server.model.items;

import constants.*;
import server.model.cards.*;

/**
 * 
 */
public class AmmoCube extends Ownable implements AmmoTileItem {

    private Color color;

    public AmmoCube(Color c) {
        this.color = c;
    }


    /**
     * Reverses the value of valid.
     */
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
        return this.color == ammoCubeObject.getColor();
    }
}