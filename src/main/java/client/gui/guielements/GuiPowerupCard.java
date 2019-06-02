package client.gui.guielements;

import constants.Color;
import javafx.scene.image.ImageView;

public class GuiPowerupCard extends ImageView {

    private int index;

    private Color color;

    private String name;

    public GuiPowerupCard(String name, Color color,String path, int index){
        super(path);
        this.color = color;
        this.name = name;
        this.index = index;
    }


    public int getIndex() {
        return index;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
