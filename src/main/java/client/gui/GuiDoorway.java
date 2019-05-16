package client.gui;

import constants.Color;
import javafx.scene.paint.Paint;

public class GuiDoorway extends GuiTile {

    public static final String DEFAULT_COLOR = Color.GREY.getHexValue();

    public GuiDoorway(int height, int width, Paint paint) {
        super(height, width, paint);
    }


}
