package client.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class GuiSquare extends GuiTile {

    private Label ammoLabel;


    public GuiSquare(int height, int width, Paint paint) {
        super(height, width, paint);
        ammoLabel = new Label("AMMO");
        ammoLabel.setAlignment(Pos.CENTER);
        this.getChildren().add(ammoLabel);
    }

    public void drawAmmo(){
        this.ammoLabel.setDisable(true);
    }

    public void restoreAmmo(){
        this.ammoLabel.setDisable(false);
    }
}
