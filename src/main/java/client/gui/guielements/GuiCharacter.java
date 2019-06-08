package client.gui.guielements;

import constants.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import server.model.player.PlayerBoard;
import view.PlayerBoardAnswer;

public class GuiCharacter extends StackPane {

    private GuiTile position;

    private Circle circle;

    private Text name;

    public GuiCharacter(GuiSpawnPoint spawnPoint, PlayerBoard playerBoard, String playerName){
        super();
        circle = new Circle();
        name = new Text(playerName);
        this.position = spawnPoint;
        spawnPoint.getChildren().add(this);
        center(spawnPoint);
        circle.setStrokeWidth(2.0);
        circle.setStroke(Paint.valueOf("#000000"));
        circle.setFill(Paint.valueOf(Color.fromCharacter(playerBoard.getCharacterName()).getNormalColor()));
        this.getChildren().add(circle);
        this.getChildren().add(name);
    }

    public void setPosition(GuiTile tile){
        this.position.getChildren().remove(this);
        tile.getChildren().add(this);
        center(tile);
        this.position = tile;
    }

    private void center(GuiTile tile){
        circle.setCenterX(tile.getLayoutX() + tile.getSide());
        circle.setCenterY(tile.getLayoutY() + tile.getSide());
        circle.setRadius(tile.getSide()/6.0);
    }

    public GuiTile getPosition() {
        return position;
    }
}
