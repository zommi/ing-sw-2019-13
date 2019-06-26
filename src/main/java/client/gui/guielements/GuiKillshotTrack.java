package client.gui.guielements;

import constants.Color;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import server.model.gameboard.KillshotTrack;

public class GuiKillshotTrack extends StackPane {
    private KillshotTrack track;

    public GuiKillshotTrack(KillshotTrack track){
        this.track = track;
        GridPane killshotPane = new GridPane();
        for(int i = 0; i < track.getInitialSkulls(); i++){
            if(track.getDamageTokens()[i] == null) {
                ImageView skull = new ImageView(getClass().getResource("/redskull.png").toExternalForm());
                killshotPane.add(skull, i, 0);
                skull.prefWidth(25.0);
                skull.prefHeight(25.0);
                skull.setFitHeight(25.0);
                skull.setFitWidth(25.0);
            }else{
                StackPane stackPane = new StackPane();
                Circle damageCircle = new Circle();
                Label label = new Label(String.valueOf(track.getMulticiplity(i)));
                label.setAlignment(Pos.CENTER);
                damageCircle.setRadius(10.0);
                damageCircle.setStrokeWidth(2.0);
                damageCircle.setStroke(Paint.valueOf("#000000"));
                damageCircle.setFill(Paint.valueOf(track.getDamageTokens()[i].getNormalColor()));
                stackPane.getChildren().add(damageCircle);
                stackPane.getChildren().add(label);
                killshotPane.add(stackPane,i,0);
            }
        }
        this.getChildren().add(killshotPane);
    }
}
