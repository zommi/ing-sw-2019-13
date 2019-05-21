package client;
import java.io.Serializable;

public class MoveInfo implements Serializable, Info {

    private int coordinateX;
    private int coordinateY;

    public MoveInfo(int x, int y){
        this.coordinateX = x;
        this.coordinateY = y;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }
}
