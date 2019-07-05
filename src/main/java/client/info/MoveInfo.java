package client.info;
import java.io.Serializable;

/**
 * Info: send the move action to the server
 */
public class MoveInfo implements Serializable, Info {

    /**
     * destination row
     */
    private int row;
    /**
     * destination col
     */
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
