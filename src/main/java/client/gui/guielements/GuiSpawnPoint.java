package client.gui.guielements;

import constants.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import server.model.cards.WeaponCard;
import server.model.map.SpawnPoint;
import server.model.map.SquareAbstract;

import java.util.ArrayList;
import java.util.List;

public class GuiSpawnPoint extends GuiTile {

    List<String> cardsOnSpawnPoint;

    private Color color;

    private SpawnPoint spawnPoint;

    public GuiSpawnPoint(int x, int y,int height, int width, Color color){
        super(x,y,height,width,Paint.valueOf(color.getSpHexValue()));
        this.color = color;
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

    public void setSpawnPoint(SpawnPoint spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public void setCardsOnSpawnPoint(SpawnPoint sp) {
        List<WeaponCard> cards = sp.getWeaponCards();
        for(int i = 0; i < 3; i++){
            cardsOnSpawnPoint.add(cards.get(i).getPath());
        }
    }

    public Color getColor() {
        return color;
    }

    public void restore(String card, int index){
        this.cardsOnSpawnPoint.add(index,card);
    }

    @Override
    public void restore() {
        this.cardsOnSpawnPoint.clear();
        for(int i = 0; i < this.spawnPoint.getWeaponCards().size(); i++){
            this.cardsOnSpawnPoint.add(this.spawnPoint.getWeaponCards().get(i).getPath());
        }
    }

    @Override
    public void setSquare(SquareAbstract square) {
        this.spawnPoint = (SpawnPoint) square;
    }

    public SpawnPoint getSpawnPoint() {
        return spawnPoint;
    }
}
