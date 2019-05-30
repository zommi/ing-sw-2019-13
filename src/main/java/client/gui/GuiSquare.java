package client.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class GuiSquare extends GuiTile {

    private final double X_OFFSET = 5.0;
    private final double Y_OFFSET = 5.0;


    public GuiSquare(int x, int y, int height, int width, Paint paint) {
        super(x, y, height, width, paint);
    }

    public void setAmmo(GuiAmmoTile ammo){
        this.getChildren().add(ammo);
        ammo.setX(this.getLayoutX() + X_OFFSET);
        ammo.setY(this.getLayoutY() + Y_OFFSET);
        ammo.setFitWidth(getSide() / 3);
        ammo.setFitHeight(getSide() / 3);
    }

}
