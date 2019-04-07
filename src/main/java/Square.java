
import java.util.*;

public class Square extends SquareAbstract {

    private AmmoTile ammoTile;
    private ArrayList<Character> charactersList;
    private Room room;
    private Color color
    private int xValue;
    private int yValue;
    // the following are just for visible squares
    private SquareAbstract nSquare;
    private SquareAbstract wSquare;
    private SquareAbstract eSquare;
    private SquareAbstract sSquare;

    public Square(int x, int y, char color) {
        super(x,y,color);



    }

    public AmmoTile getAmmoTile() {
        return ammoTile;
    }

    public void setAmmoTile(AmmoTile ammoTile) {
        this.ammoTile = ammoTile;
    }

    public void removeAmmoTile(){
        ammoTile = null;
    }
}