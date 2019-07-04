package client.info;
import java.io.Serializable;

public class MoveInfo implements Serializable, Info {

    private int row;
    private int col;

    public MoveInfo(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
