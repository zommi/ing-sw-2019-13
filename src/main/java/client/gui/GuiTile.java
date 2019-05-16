package client.gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GuiTile extends StackPane {


    public GuiTile(int height, int width, Paint paint){
        super();
        this.getChildren().add(new Rectangle(height,width,paint));
    }

}