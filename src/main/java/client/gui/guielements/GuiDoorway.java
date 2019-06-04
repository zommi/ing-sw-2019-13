package client.gui.guielements;

import constants.Color;
import javafx.scene.paint.Paint;
import server.model.map.SquareAbstract;

public class GuiDoorway extends GuiTile {

    public static final String DEFAULT_COLOR = Color.GREY.getHexValue();

    public GuiDoorway(int height, int width, Paint paint) {
        super(height, width, paint);
    }

    @Override
    public void restore() {

    }

    @Override
    public void setSquare(SquareAbstract square) {

    }


}
