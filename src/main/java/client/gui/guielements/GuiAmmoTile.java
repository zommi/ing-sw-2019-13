package client.gui.guielements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class used to represent ammotiles on squares
 */
public class GuiAmmoTile extends ImageView {

    public GuiAmmoTile(String path){
        super();
        this.setImage(new Image(path));
    }


}
