import java.util.List;

public interface SquareInterface {

    public Room getRoom();


    public SquareInterface geteSquare();
    public void seteSquare(SquareInterface eSquare);
    public SquareInterface getnSquare();
    public void setnSquare(SquareInterface nSquare);
    public SquareInterface getwSquare();
    public void setwSquare(SquareInterface wSquare);
    public SquareInterface getsSquare();
    public void setsSquare(SquareInterface sSquare);
    public int getxValue();
    public int getyValue();
    public void removeCharacter(Character character);

    public List<Square> getAdjacentSquares();
}
