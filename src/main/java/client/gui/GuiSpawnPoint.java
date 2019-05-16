package client.gui;

import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import server.model.cards.WeaponCard;
import server.model.map.SpawnPoint;

import java.util.ArrayList;
import java.util.List;

public class GuiSpawnPoint extends GuiTile {

    List<String> cardsOnSpawnPoint;

    public GuiSpawnPoint(int height, int width, Paint paint){
        super(height,width,paint);
        Rectangle overlay = new Rectangle(height / 1.5 , width / 1.5 , Paint.valueOf("#000000"));
        overlay.setStroke(Paint.valueOf("#000000"));
        overlay.setFill(Paint.valueOf("#00000000"));
        overlay.setStrokeWidth(10);
        this.getChildren().add(overlay);
        cardsOnSpawnPoint = new ArrayList<>();
    }

    public List<String> getCardsOnSpawnPoint() {
        return cardsOnSpawnPoint;
    }

    public void setCardsOnSpawnPoint(SpawnPoint sp) {
        List<WeaponCard> cards = sp.getWeaponCards();
        for(int i = 0; i < 3; i++){
            cardsOnSpawnPoint.add(cards.get(i).getPath());
        }
    }

    public void restore(String card, int index){
        this.cardsOnSpawnPoint.add(index,card);
    }
}
