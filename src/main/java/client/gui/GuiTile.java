package client.gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GuiTile extends StackPane {

    private int side;

    private int x;

    private int y;

    public GuiTile(int height, int width, Paint paint){
        super();
        this.side = height;
        this.getChildren().add(new Rectangle(height,width,paint));
    }


    public GuiTile(int x, int y, int height, int width, Paint paint){
        super();
        this.x = x;
        this.y = y;
        this.side = height;
        this.getChildren().add(new Rectangle(height,width,paint));
    }

    public int getSide() {
        return side;
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }
}