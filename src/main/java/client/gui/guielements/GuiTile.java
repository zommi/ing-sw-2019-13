package client.gui.guielements;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import server.model.map.Square;
import server.model.map.SquareAbstract;

public abstract class GuiTile extends StackPane {

    private int side;

    private int col;

    private int row;

    public GuiTile(int height, int width, Paint paint){
        super();
        this.side = height;
        this.getChildren().add(new Rectangle(height,width,paint));
    }


    public GuiTile(int row, int col, int height, int width, Paint paint){
        super();
        this.col = col;
        this.row = row;
        this.side = height;
        this.getChildren().add(new Rectangle(height,width,paint));
    }

    public int getSide() {
        return side;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    public abstract void restore();

    public abstract void setSquare(SquareAbstract square);

    public abstract SquareAbstract getSquare();
}