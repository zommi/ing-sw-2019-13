package client.gui.guielements;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import server.model.map.Square;
import server.model.map.SquareAbstract;

public abstract class GuiTile extends StackPane {

    private int side;

    private int col;

    private int row;

    private VBox box;

    public GuiTile(int height, int width, Paint paint){
        super();
        box = new VBox();
        this.side = height;
        this.getChildren().add(new Rectangle(height,width,paint));
        this.getChildren().add(box);
    }


    public GuiTile(int row, int col, int height, int width, Paint paint){
        super();
        box = new VBox();
        this.col = col;
        this.row = row;
        this.side = height;
        this.getChildren().add(new Rectangle(height,width,paint));
        box.setAlignment(Pos.CENTER);
        this.getChildren().add(box);
        this.getChildren().get(this.getChildren().size()-1).toFront();
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

    public void addCharacter(GuiCharacter character){
        this.box.getChildren().add(character);
    }

    public void removeCharacter(GuiCharacter character){
        this.box.getChildren().remove(character);
    }

    public abstract void restore();

    public abstract void setSquare(SquareAbstract square);

    public abstract SquareAbstract getSquare();
}