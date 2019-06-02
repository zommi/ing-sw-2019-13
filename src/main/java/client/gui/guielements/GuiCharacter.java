package client.gui.guielements;

import constants.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class GuiCharacter extends Circle {

    private GuiTile position;

    public GuiCharacter(GuiSpawnPoint spawnPoint, Color color){
        super();
        this.position = spawnPoint;
        spawnPoint.getChildren().add(this);
        center(spawnPoint);
        setStrokeWidth(2.0);
        setStroke(Paint.valueOf("#000000"));
        setFill(Paint.valueOf(color.getHexValue()));
    }

    public void setPosition(GuiTile tile){
        this.position.getChildren().remove(this);
        tile.getChildren().add(this);
        center(tile);
        this.position = tile;
    }

    private void center(GuiTile tile){
        setCenterX(tile.getLayoutX() + tile.getSide());
        setCenterY(tile.getLayoutY() + tile.getSide());
        setRadius(tile.getSide()/6.0);
    }
}
