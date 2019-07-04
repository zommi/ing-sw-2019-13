package client.info;

import java.io.Serializable;

public class SquareInfo  implements Serializable,Info {
    private int row;
    private int col;

    public SquareInfo(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
