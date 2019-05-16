package client.gui;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class GuiWeaponCard extends ImageView {

    private int index;

    public GuiWeaponCard(String path, int index){
        super(path);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }


}
