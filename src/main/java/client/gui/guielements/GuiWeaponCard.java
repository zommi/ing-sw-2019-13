package client.gui.guielements;

import constants.Color;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class GuiWeaponCard extends ImageView {

    private int index;

    private String name;

    public GuiWeaponCard(String path, int index){
        super(path);
        this.index = index;
    }

    public GuiWeaponCard(String name, String path, int index){
        super(path);
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

}
