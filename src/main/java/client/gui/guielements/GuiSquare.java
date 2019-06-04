package client.gui.guielements;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import server.model.map.Square;
import server.model.map.SquareAbstract;

public class GuiSquare extends GuiTile {

    private GuiAmmoTile ammo;

    private StackPane ammoLayer;

    private Square square;

    public GuiSquare(int x, int y, int height, int width, Paint paint) {
        super(x, y, height, width, paint);
        this.ammoLayer = new StackPane();
        this.getChildren().add(ammoLayer);
    }

    public void setAmmo(GuiAmmoTile ammoToAdd){
        this.ammo = ammoToAdd;
        this.ammoLayer.getChildren().add(ammoToAdd);
        this.ammoLayer.setAlignment(Pos.BOTTOM_RIGHT);
        ammoToAdd.setFitWidth(getSide() / 3);
        ammoToAdd.setFitHeight(getSide() / 3);
    }

    public GuiAmmoTile getAmmo() {
        return ammo;
    }

    public void removeAmmo() {
        this.ammoLayer.getChildren().remove(ammo);
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    @Override
    public void restore() {
        removeAmmo();
        if(this.square.getAmmoTile() != null) {
            GuiAmmoTile ammoToRestore = new GuiAmmoTile(this.square.getAmmoTile().getPath());
            this.ammo = ammoToRestore;
            this.ammoLayer.setAlignment(Pos.BOTTOM_RIGHT);
            ammoToRestore.setFitWidth(getSide() / 3);
            ammoToRestore.setFitHeight(getSide() / 3);
            this.ammoLayer.getChildren().add(ammoToRestore);
        }
    }

    @Override
    public void setSquare(SquareAbstract square) {
        this.square = (Square) square;
    }

}
