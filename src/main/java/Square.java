
import java.util.*;

public class Square extends SquareAbstract {

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

    public Room getRoom(){
        return room;
    }

    public Color getColor() {
        return room.getColor();
    }

    public List<Square> getAdjacentSquares() {
        ArrayList<Square> squaresList= new ArrayList<Square>();
        if(nSquare != null)
            squaresList.add(nSquare);
        if(sSquare != null)
            squaresList.add(sSquare);
        if(wSquare != null)
            squaresList.add(wSquare);
        if(eSquare != null)
            squaresList.add(eSquare);
        return squaresList;
    }

}