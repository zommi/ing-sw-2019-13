package constants;

public enum Color {
    RED("r",0),
    BLUE("b",1),
    YELLOW("y",2),
    GREEN("g",3),
    WHITE("w",4),
    PURPLE("p",5),
    GREY("g",6),
    UNDEFINED("u",7);

    private int index;
    private String abbreviation;

    private Color(String abbreviation, int index) {
        this.abbreviation = abbreviation;
        this.index = index;
    }
    public String getAbbreviation() { return abbreviation; }

    public static Color fromString(String s) {
        for (Color color : Color.values()) {
            if (color.abbreviation.equalsIgnoreCase(s)) {
                return color;
            }
        }
        return null;
    }

    public static Color fromIndex(int index){
        for(Color c : Color.values()){
            if(c.index == index) return c;
        }
        return null;
    }

    @Override
    public String toString() {
        return abbreviation;
    }

    public int getIndex() {
        return index;
    }

}
