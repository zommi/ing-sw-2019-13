package view;

public class MapElement {
    private int x;
    private int y;
    private String character;

    public MapElement(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.character = name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getCharacter() {
        return character;
    }
}
