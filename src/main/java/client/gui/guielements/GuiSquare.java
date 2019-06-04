package client.gui.guielements;

import javafx.scene.paint.Paint;
import server.model.map.Square;
import server.model.map.SquareAbstract;

public class GuiSquare extends GuiTile {

    private final double X_OFFSET = 5.0;
    private final double Y_OFFSET = 5.0;
    private GuiAmmoTile ammo;
    private Square square;


    public GuiSquare(int x, int y, int height, int width, Paint paint) {
        super(x, y, height, width, paint);
    }

    public void setAmmo(GuiAmmoTile ammoToAdd){
        this.ammo = ammoToAdd;
        this.getChildren().add(ammoToAdd);
        ammoToAdd.setFitWidth(getSide() / 3);
        ammoToAdd.setFitHeight(getSide() / 3);
    }

    public GuiAmmoTile getAmmo() {
        return ammo;
    }

    public void removeAmmo() {
        this.getChildren().remove(ammo);
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    @Override
    public void restore() {
        removeAmmo();
        GuiAmmoTile ammoToRestore = new GuiAmmoTile(this.square.getAmmoTile().getPath());
        this.ammo = ammoToRestore;
        ammoToRestore.setFitWidth(getSide() / 3);
        ammoToRestore.setFitHeight(getSide() / 3);
        this.getChildren().add(ammoToRestore);
    }

    @Override
    public void setSquare(SquareAbstract square) {
        this.square = (Square) square;
    }

}
