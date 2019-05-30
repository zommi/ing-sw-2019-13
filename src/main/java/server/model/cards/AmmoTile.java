package server.model.cards;

import java.io.Serializable;
import java.util.*;
import server.model.items.*;

/**
 * 
 */
public class AmmoTile implements CollectableInterface, Serializable {

    private List<AmmoCube> content;
    private boolean hasPowerup;
    private String path;

    public AmmoTile(List<AmmoCube> content, boolean hasPoweurup) {
        this.content = content;
        this.hasPowerup = hasPoweurup;
        this.path = "";
    }

    public AmmoTile(List<AmmoCube> content, boolean hasPoweurup, String path) {
        this.content = content;
        this.hasPowerup = hasPoweurup;
        this.path = path;
    }

    public List<AmmoCube> getContent() {
        return content;
    }

    public boolean hasPowerup(){
        return hasPowerup;
    }

    @Override
    public String toString() {
        String stringToReturn = "";
        for(AmmoCube cube : content) stringToReturn+= cube.toString() + '\n';
        if(hasPowerup) stringToReturn += "has powerup\n";
        return stringToReturn;
    }

    public String getPath() {
        return path;
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