package Cards;

import java.util.*;
import Items.*;

/**
 * 
 */
public class AmmoTile implements CollectableInterface {

    private List<AmmoCube> content;
    private boolean hasPowerup;

    public AmmoTile(List<AmmoCube> content, boolean hasWeapon) {
        this.content = content;
        this.hasPowerup = hasWeapon;
    }

    public List<AmmoCube> getContent() {
        return content;
    }

    public boolean hasWeapon(){
        return hasPowerup;
    }

    @Override
    public String toString() {
        String stringToReturn = "";
        for(AmmoCube cube : content) stringToReturn+= cube.toString() + '\n';
        if(hasPowerup) stringToReturn += "has powerup\n";
        return stringToReturn;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof AmmoTile)) {
            return false;
        }

        AmmoTile ammoTileObject = (AmmoTile) obj;

        if(this.getContent().size() != ammoTileObject.getContent().size()
            || this.hasPowerup != ammoTileObject.hasPowerup) return false;
        return this.content.containsAll(ammoTileObject.getContent());
    }
}